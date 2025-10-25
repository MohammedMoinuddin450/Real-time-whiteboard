package com.Banking_application.Persistence.service.Repos;

import com.Banking_application.Persistence.service.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {
    List<Board> findAllByOwnerId(Long userId);
}
