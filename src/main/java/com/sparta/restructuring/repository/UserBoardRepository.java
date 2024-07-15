package com.sparta.restructuring.repository;

import com.sparta.restructuring.entity.Board;
import com.sparta.restructuring.entity.User;
import com.sparta.restructuring.entity.UserBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBoardRepository extends JpaRepository<UserBoard, Long> {
    Optional<UserBoard> findByBoardAndUser(Board board, User user);
    UserBoard findByUser(User user);

}
