package com.Banking_application.Persistence.service.controller;

import com.Banking_application.Persistence.service.model.Board;
import com.Banking_application.Persistence.service.model.Dtos.BoardCreateDTO;
import com.Banking_application.Persistence.service.model.Dtos.BoardUpdateDTO;
import com.Banking_application.Persistence.service.service.BoardService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/board")
@AllArgsConstructor
@NoArgsConstructor
public class boardController {
    private BoardService boardService;

    @PostMapping
    public ResponseEntity<Board> createBoard(@RequestHeader("X-User-ID") Long userId,
                                             @RequestBody BoardCreateDTO boardCreateDTO) {
        Board board = boardService.createBoard(boardCreateDTO, userId);
        return new ResponseEntity<>(board, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Board>> getAllBoards(@RequestHeader("X-User-ID") Long userId) {
        List<Board> boards = boardService.getAllBoards(userId);
        return ResponseEntity.ok(boards);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<Board> getBoardById(@RequestHeader("X-User-ID") Long userId,@PathVariable Long boardId) {
        Board board = boardService.getBoardById(boardId);
        return board != null ? ResponseEntity.ok(board) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(@RequestHeader("X-User-ID") Long userId,
                                            @PathVariable Long boardId) {
        boolean isDeleted = boardService.deleteBoard(userId,boardId);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<Board> updateBoard(@RequestHeader("X-User-ID") Long userId,
                                             @PathVariable Long boardId, @RequestBody BoardUpdateDTO boardUpdateDTO) {
        Board board = boardService.updateBoard(boardId, boardUpdateDTO,userId);
        return board != null ? ResponseEntity.ok(board) : ResponseEntity.notFound().build();
    }
}
