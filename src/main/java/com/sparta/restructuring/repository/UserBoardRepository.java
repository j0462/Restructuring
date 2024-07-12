package com.sparta.restructuring.repository;

import com.sparta.restructuring.entity.UserBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBoardRepository extends JpaRepository<UserBoard, Long> {
    Optional<UserBoard> findByBoardIdAndUserId(Long boardId, Long id);
}
