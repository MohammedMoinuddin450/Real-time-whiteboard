package com.Banking_application.Persistence.service.controller;

import com.Banking_application.Persistence.service.model.Dtos.ElementDTO;
import com.Banking_application.Persistence.service.model.Element;
import com.Banking_application.Persistence.service.service.ElementService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/boards/{boardId}/elements")
@AllArgsConstructor
@NoArgsConstructor
public class elementController {

    private ElementService elementService;

    @PostMapping
    public ResponseEntity<Element> createElement(@RequestHeader("X-User-ID") Long userId,@PathVariable Long boardId, @RequestBody ElementDTO elementDTO) throws JsonProcessingException {
        Element element = elementService.createElement(boardId, elementDTO, userId);
        return new ResponseEntity<>(element, HttpStatus.CREATED);
    }

    @GetMapping("/allelements")
    public ResponseEntity<List<Element>> getAllElements(@RequestHeader("X-User-ID") Long userId,@PathVariable Long boardId) {
        List<Element> elements = elementService.getAllElements(boardId, userId);
        return ResponseEntity.ok(elements);
    }

    @GetMapping("/{elementId}")
    public ResponseEntity<Element> getElementById(@RequestHeader("X-User-ID") Long userId,@PathVariable Long boardId, @PathVariable Long elementId) {
        Element element = elementService.getElementById(boardId, elementId,userId);
        return element != null ? ResponseEntity.ok(element) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{elementId}")
    public ResponseEntity<Element> updateElement(@RequestHeader("X-User-ID") Long userId,@PathVariable Long boardId, @PathVariable Long elementId, @RequestBody ElementDTO elementDTO) throws JsonProcessingException {
        Element element = elementService.updateElement(boardId, elementId, elementDTO, userId);
        return element != null ? ResponseEntity.ok(element) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{elementId}")
    public ResponseEntity<Void> deleteElement(@RequestHeader("X-User-ID") Long userId,@PathVariable Long boardId, @PathVariable Long elementId) {
        boolean isDeleted = elementService.deleteElement(boardId, elementId, userId);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
