package com.Banking_application.Persistence.service.model.Dtos;

import com.Banking_application.Persistence.service.model.BoardRole;

public record BoardMemberDTO(Long userId, BoardRole role) {
}
