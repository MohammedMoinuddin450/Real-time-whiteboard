package com.Banking_application.Persistence.service.Repos;

import com.Banking_application.Persistence.service.model.Element;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ElementRepo extends JpaRepository<Element,Long> {
    List<Element> findByBoard_boardId(Long boardId);

    Optional<Element> findByIdAndBoard_boardId(Long elementId, Long boardId);
}
