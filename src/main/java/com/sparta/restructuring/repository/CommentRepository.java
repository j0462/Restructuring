package com.sparta.restructuring.repository;

import com.sparta.restructuring.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByCardIdOrderByCreateAtDesc(Long cardId);
}
