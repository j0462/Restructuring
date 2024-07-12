package com.sparta.restructuring.repository;

import com.sparta.restructuring.entity.User;
import com.sparta.restructuring.entity.UserBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBoardRepository extends JpaRepository<UserBoard, Long> {
    Optional<UserBoard> findByIdAndUserId(Long boardId, Long id);
    UserBoard findByUser(User user);

}
