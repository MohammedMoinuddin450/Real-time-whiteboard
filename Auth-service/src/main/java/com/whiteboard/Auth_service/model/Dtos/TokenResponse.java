package com.whiteboard.Auth_service.model.Dtos;

public record TokenResponse(String accessToken,
                            String refreshToken
                            ) {
}
