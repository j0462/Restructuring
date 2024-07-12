package com.sparta.restructuring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.restructuring.entity.UserBoard;

public interface UserBoardRepository extends JpaRepository<UserBoard, Long> {
}
