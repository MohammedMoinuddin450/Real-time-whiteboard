package com.whiteboard.Auth_service.service;

import com.whiteboard.Auth_service.model.Dtos.TokenResponse;
import com.whiteboard.Auth_service.model.Roles;
import com.whiteboard.Auth_service.model.Token;
import com.whiteboard.Auth_service.model.User;
import com.whiteboard.Auth_service.repo.tokenRepo;
import com.whiteboard.Auth_service.repo.userRepo;
import com.whiteboard.Auth_service.config.jwtutil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
//@AllArgsConstructor
//@NoArgsConstructor
public class authService {

    @Value("${CLIENT_ID}")
    private String client_id;

    @Value("${CLIENT_SECRET}")
    private String client_secret;

    @Autowired
    private tokenRepo tRepo;
    @Autowired
    private userRepo urepo;
    @Autowired
    private jwtutil util;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String getGoogleOAuthRedirectUrl() {
        return "https://accounts.google.com/o/oauth2/v2/auth?" +
                "response_type=code" +
                "&client_id=" + client_id +
                "&redirect_uri=" + "http://localhost:8080/oauth/callback" +
                "&scope=openid email profile" +
                "&access_type=offline"+  // Ensures a refresh token is returned
                "&prompt=consent";
    }

    public TokenResponse handleOAuthCallback(String code) {
        try {

            String tokenEndpoint= "https://oauth2.googleapis.com/token";
            MultiValueMap<String,String> params=new LinkedMultiValueMap<>();
            params.add("code",code);
            params.add("client_id",client_id);
            params.add("client_secret",client_secret);
//            params.add("redirect_uri", "https://developers.google.com/oauthplayground");
            params.add("redirect_uri","http://localhost:8080/oauth/callback");
            params.add("grant_type","authorization_code");
            params.add("access_type", "offline");

            System.out.println(params.get(client_id+client_secret));
            HttpHeaders headers=new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String,String>> request=new HttpEntity<>(params,headers);

            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenEndpoint, request, Map.class);
            System.out.println("Response from token endpoint: " + tokenResponse.getBody());
            String refreshToken = (String) tokenResponse.getBody().get("refresh_token");
            System.out.println("refresh:"+refreshToken);
            String idToken = (String) tokenResponse.getBody().get("id_token");
            String userInfoUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
            ResponseEntity<Map> userInfoResponse = restTemplate.getForEntity(userInfoUrl, Map.class);

            if (userInfoResponse.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("Failed to verify ID token with Google");
            }

            Map<String, Object> userInfo = userInfoResponse.getBody();
            String email = (String) userInfo.get("email");
            String name =(String) userInfo.get("name");
            User user = urepo.findByEmail(email)
                    .orElseGet(() -> {
                        User newUser = new User();
                        newUser.setEmail(email);
                        newUser.setUsername(name);
                        newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                        newUser.setRole(Roles.valueOf("USER"));
                        return urepo.save(newUser);
                        });

            if (refreshToken != null) {
           // String hashedRefreshToken =hashRefreshToken(refreshToken);
                Token refreshTokenEntity = new Token();
                refreshTokenEntity.setUser(user);
                refreshTokenEntity.setRefreshToken(refreshToken);
                refreshTokenEntity.setIssuedAt(Instant.now());
                refreshTokenEntity.setExpiresAt(Instant.now().plus(30, ChronoUnit.DAYS));  // Set the expiration date for 30 days
                refreshTokenEntity.setRevoked(false);
                tRepo.save(refreshTokenEntity);
            }

            String jwtToken = util.generateToken(user);
            return new TokenResponse(jwtToken,refreshToken);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String hashRefreshToken(String refreshToken) {
        return BCrypt.hashpw(refreshToken, BCrypt.gensalt(10));
    }


    public TokenResponse refreshAccessToken(String refreshToken) {
        Token token = tRepo.findByRefreshToken(refreshToken);
        if (token == null || token.isRevoked()) {
            throw new RuntimeException("Invalid or revoked refresh token");
        }
        User user = token.getUser();

        String newAccessToken = util.generateToken(user);

        String newRefreshToken = util.generateRefreshToken(user.getEmail());
        token.setRefreshToken(newRefreshToken);
        token.setIssuedAt(Instant.now());
        token.setExpiresAt(Instant.now().plus(30, ChronoUnit.MINUTES));
        token.setRevoked(false);
        tRepo.save(token);
        return new TokenResponse(newAccessToken, newRefreshToken);
    }



    public void logout(String s) {
        User user = urepo.findByUsername(s)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Token> userTokens = tRepo.findByUser(user);
        for (Token token : userTokens) {
            token.setRevoked(true);
            tRepo.save(token);
        }
    }
}
