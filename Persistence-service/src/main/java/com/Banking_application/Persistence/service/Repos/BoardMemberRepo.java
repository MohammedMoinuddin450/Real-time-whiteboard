package com.Banking_application.Persistence.service.Repos;

import com.Banking_application.Persistence.service.model.boardMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardMemberRepo extends JpaRepository<boardMember,Long> {
    List<boardMember> findByBoard_boardId(Long boardId);

    boolean existsByBoard_boardIdAndUserId(Long boardId, Long requesterId);

    Optional<boardMember> findByBoard_boardIdAndUserId(Long boardId, Long userId);
}
