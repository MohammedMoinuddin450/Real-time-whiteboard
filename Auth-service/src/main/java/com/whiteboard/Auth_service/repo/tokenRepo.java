package com.whiteboard.Auth_service.repo;

import com.whiteboard.Auth_service.model.Token;
import com.whiteboard.Auth_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface tokenRepo extends JpaRepository<Token,Long> {

    List<Token> findByUser(User user);

    Token findByRefreshToken(String refreshToken);
}
