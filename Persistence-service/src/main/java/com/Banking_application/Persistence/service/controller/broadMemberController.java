package com.Banking_application.Persistence.service.controller;

import com.Banking_application.Persistence.service.model.Dtos.BoardMemberDTO;
import com.Banking_application.Persistence.service.model.boardMember;
import com.Banking_application.Persistence.service.service.BoardMemberService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/boards/{boardId}/members")
@AllArgsConstructor
@NoArgsConstructor
public class broadMemberController {
    private BoardMemberService boardMemberService;

    @PostMapping
    public ResponseEntity<boardMember> addMember(
            @RequestHeader("X-User-ID")Long requesterId,
            @PathVariable Long boardId,
            @RequestBody BoardMemberDTO memberDTO) throws AccessDeniedException {

        boardMember member = boardMemberService.addMember(boardId, memberDTO, requesterId);
        return new ResponseEntity<>(member, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<boardMember>> getMembers(
            @RequestHeader("X-User-ID") Long requesterId,
            @PathVariable Long boardId) throws AccessDeniedException {

        List<boardMember> members = boardMemberService.getMembers(boardId, requesterId);
        return ResponseEntity.ok(members);
    }

    @PatchMapping("/{memberId}/role")
    public ResponseEntity<boardMember> updateRole(
            @RequestHeader("X-User-ID") Long requesterId,
            @PathVariable Long boardId,
            @PathVariable Long memberId,
            @RequestBody String newRole) throws AccessDeniedException {

        boardMember member = boardMemberService.updateRole(boardId, memberId, newRole,requesterId);
        return ResponseEntity.ok(member);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> removeMember(
            @RequestHeader("X-User-ID") Long requesterId,
            @PathVariable Long boardId,
            @PathVariable Long memberId) throws AccessDeniedException {

        boolean removed = boardMemberService.removeMember(boardId, requesterId, memberId);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
