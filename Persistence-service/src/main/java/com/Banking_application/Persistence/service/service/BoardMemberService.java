package com.Banking_application.Persistence.service.service;

import com.Banking_application.Persistence.service.Repos.BoardMemberRepo;
import com.Banking_application.Persistence.service.Repos.BoardRepository;
import com.Banking_application.Persistence.service.model.Board;
import com.Banking_application.Persistence.service.model.BoardRole;
import com.Banking_application.Persistence.service.model.Dtos.BoardMemberDTO;
import com.Banking_application.Persistence.service.model.boardMember;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@AllArgsConstructor
public class BoardMemberService {
    private final BoardRepository boardRepository;
    private final BoardMemberRepo boardMemberRepo;

    @Transactional
    public boardMember addMember(Long boardId, BoardMemberDTO dto, Long requesterId) throws AccessDeniedException {
        Board board = getBoardOrThrow(boardId);
        validateBoardOwnership(board, requesterId);

        boardMember member = new boardMember();
        member.setBoard(board);
        member.setUserId(dto.userId());
        member.setRole(dto.role());
        return boardMemberRepo.save(member);
    }

    public List<boardMember> getMembers(Long boardId, Long requesterId) throws AccessDeniedException {
        Board board = getBoardOrThrow(boardId);
        validateBoardAccess(board, requesterId);
        return boardMemberRepo.findByBoard_boardId(boardId);
    }

    @Transactional
    public boardMember updateRole(Long boardId, Long memberId, String newRole, Long requesterId) throws AccessDeniedException {
        Board board = getBoardOrThrow(boardId);
        validateBoardOwnership(board, requesterId);

        boardMember member = boardMemberRepo.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("BoardMember not found"));
        try {
            member.setRole(BoardRole.valueOf(newRole.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + newRole);
        }
        return boardMemberRepo.save(member);
    }

    @Transactional
    public boolean removeMember(Long boardId, Long memberId, Long requesterId) throws AccessDeniedException {
        Board board = getBoardOrThrow(boardId);
        validateBoardOwnership(board, requesterId);

        boardMember member = boardMemberRepo.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("BoardMember not found"));
        boardMemberRepo.delete(member);
        return true;
    }

    private Board getBoardOrThrow(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));
    }

    private void validateBoardOwnership(Board board, Long requesterId) throws AccessDeniedException {
        if (!board.getOwnerId().equals(requesterId)) {
            throw new AccessDeniedException("Only the owner can modify board members");
        }
    }

    private void validateBoardAccess(Board board, Long requesterId) throws AccessDeniedException {
        boolean isMember = boardMemberRepo.existsByBoard_boardIdAndUserId(board.getBoardId(), requesterId);
        if (!isMember && !board.getOwnerId().equals(requesterId)) {
            throw new AccessDeniedException("You are not a member of this board");
        }
    }
}
