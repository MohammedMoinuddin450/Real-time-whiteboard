package com.whiteboard.Auth_service.controller;

import com.whiteboard.Auth_service.model.Dtos.*;
import com.whiteboard.Auth_service.service.authService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collections;

@RestController
@RequestMapping("/oauth")
@AllArgsConstructor
//@NoArgsConstructor
public class authController {

    @Autowired
    private authService aService;

    @GetMapping("/callback")
    public ResponseEntity<TokenResponse> handleGoogleCallback(@RequestParam String code) {
        TokenResponse tokens = aService.handleOAuthCallback(code);
        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/google")
    public void login(HttpServletResponse response) throws IOException {
        String googleRedirectUrl = aService.getGoogleOAuthRedirectUrl();
        response.sendRedirect(googleRedirectUrl);
    }


    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok(aService.refreshAccessToken(request.refreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(Authentication authentication) {
        try {
            String username = authentication.getName();
            aService.logout(username);
            return ResponseEntity.ok("Logout successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Logout failed");
        }
    }

}
