package com.Banking_application.Persistence.service.service;

import com.Banking_application.Persistence.service.Repos.BoardMemberRepo;
import com.Banking_application.Persistence.service.Repos.BoardRepository;
import com.Banking_application.Persistence.service.Repos.ElementRepo;
import com.Banking_application.Persistence.service.model.Board;
import com.Banking_application.Persistence.service.model.Dtos.ElementDTO;
import com.Banking_application.Persistence.service.model.Element;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ElementService {

    private final ElementRepo elementRepo;
    private  final BoardRepository boardRepository;
    private final BoardMemberRepo boardMemberRepo;

    public Element createElement(Long boardId, ElementDTO dto, Long userId) throws JsonProcessingException {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        if (!isBoardMember(boardId, userId)) {
            throw new RuntimeException("user not a member");
        }

        Element element = new Element();
        element.setBoard(board);
        element.setType(dto.type());
        element.setData(new ObjectMapper().writeValueAsString(dto.data())); //  map to Json
        element.setX(dto.x());
        element.setY(dto.y());
        element.setWidth(dto.width());
        element.setHeight(dto.height());
        element.setCreatedBy(userId);
        return elementRepo.save(element);
    }

    private boolean isBoardMember(Long boardId, Long userId) {
        return boardMemberRepo.findByBoard_boardIdAndUserId(boardId, userId).isPresent();
    }

    public List<Element> getAllElements(Long boardId, Long userId) {
        if (!isBoardMember(boardId, userId)) {
            throw new RuntimeException("user not in board");
        }
        return elementRepo.findByBoard_boardId(boardId);
    }

    public Element getElementById(Long boardId, Long elementId, Long userId) {
        if (!isBoardMember(boardId, userId)) {
            throw new RuntimeException("user not in board");
        }

        return elementRepo.findByIdAndBoard_boardId(elementId, boardId)
                .orElseThrow(() -> new RuntimeException("element not found"));
    }

    public Element updateElement(Long boardId, Long elementId, ElementDTO dto, Long userId) throws JsonProcessingException {
        Element element = elementRepo.findByIdAndBoard_boardId(elementId, boardId)
                .orElseThrow(() -> new RuntimeException("element not found"));

        if (!element.getCreatedBy().equals(userId)) {
            throw new RuntimeException("not authorized to update element");
        }

        if (dto.data() != null)
            element.setData(new ObjectMapper().writeValueAsString(dto.data()));
        if (dto.x() != null) element.setX(dto.x());
        if (dto.y() != null) element.setY(dto.y());
        if (dto.width() != null) element.setWidth(dto.width());
        if (dto.height() != null) element.setHeight(dto.height());
        return elementRepo.save(element);
    }

    public boolean deleteElement(Long boardId, Long elementId, Long userId) {
        Element element = elementRepo.findByIdAndBoard_boardId(elementId, boardId)
                .orElseThrow(() -> new RuntimeException("Element not found"));

        if (!element.getCreatedBy().equals(userId)) {
            throw new RuntimeException("Not authorized to delete element");
        }
        elementRepo.delete(element);
        return true;
    }

}
