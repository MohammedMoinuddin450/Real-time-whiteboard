package com.whiteboard.Auth_service.service;

import com.whiteboard.Auth_service.model.Roles;
import com.whiteboard.Auth_service.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class userDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private String name;
    private String email;
    private String password;
//    private Roles role;

    public static userDetailsImpl build(User user){
        return new userDetailsImpl(user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword());
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }
}
