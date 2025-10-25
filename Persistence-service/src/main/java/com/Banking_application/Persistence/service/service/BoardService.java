package com.Banking_application.Persistence.service.service;

import com.Banking_application.Persistence.service.Repos.BoardMemberRepo;
import com.Banking_application.Persistence.service.Repos.BoardRepository;
import com.Banking_application.Persistence.service.model.Board;
import com.Banking_application.Persistence.service.model.BoardRole;
import com.Banking_application.Persistence.service.model.Dtos.BoardCreateDTO;
import com.Banking_application.Persistence.service.model.Dtos.BoardUpdateDTO;
import com.Banking_application.Persistence.service.model.boardMember;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardMemberRepo boardMemberRepository;

    public Board createBoard(BoardCreateDTO boardCreateDTO, Long userId) {
        Board board = new Board();
        board.setTitle(boardCreateDTO.title());
        board.setOwnerId(userId);  // Assign user as the owner
        board.setCreatedAt(LocalDateTime.now());
        board.setUpdatedAt(LocalDateTime.now());

        board = boardRepository.save(board);

        boardMember ownerMember = new boardMember();
        ownerMember.setBoard(board);
        ownerMember.setUserId(userId);
        ownerMember.setRole(BoardRole.valueOf("OWNER"));
        boardMemberRepository.save(ownerMember);

        return board;
    }

    public Board getBoardById(Long boardId) {
        return boardRepository.findById(boardId).orElse(null);
    }

    public List<Board> getAllBoards(Long userId) {
        return boardRepository.findAllByOwnerId(userId);
    }

    public boolean deleteBoard(Long boardId, Long userId) {
        Board board = getBoardById(boardId);
        if (board == null || !board.getOwnerId().equals(userId)) {
            return false;
        }

        boardRepository.delete(board);
        return true;
    }

    public Board updateBoard(Long boardId, BoardUpdateDTO boardUpdateDTO, Long userId) {
        Board board = getBoardById(boardId);
        if (board == null || !board.getOwnerId().equals(userId)) {
            return null;
        }
        board.setTitle(boardUpdateDTO.title());
        board.setUpdatedAt(LocalDateTime.now());
        return boardRepository.save(board);  // Save the updated board
    }
}
