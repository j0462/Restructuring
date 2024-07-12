package com.sparta.restructuring.repository;

import com.sparta.restructuring.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
