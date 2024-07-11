package com.sparta.restructuring.service;

import com.sparta.restructuring.dto.CommentRequest;
import com.sparta.restructuring.dto.CommentResponse;
import com.sparta.restructuring.entity.Card;
import com.sparta.restructuring.entity.Comment;
import com.sparta.restructuring.repository.CardRepository;
import com.sparta.restructuring.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CardRepository cardRepository;

    @Transactional
    public CommentResponse addComment(CommentRequest request, Long cardId) {
        Optional<Card> existingCard = cardRepository.findById(cardId);
        if (existingCard.isEmpty()) {
            throw new IllegalArgumentException("Card를 찾을 수 없습니다");
        }
        Comment comment = Comment.builder()
                .content(request.getContent())
                .card(existingCard.get())
                .build();
        commentRepository.save(comment);
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .build();
    }

    @Transactional
    public List<CommentResponse> getCommentsByCardId(Long cardId) {
        Optional<Card> existingCard = cardRepository.findById(cardId);
        if (existingCard.isEmpty()) {
            throw new IllegalArgumentException("Card를 찾을 수 없습니다");
        }
        List<Comment> comments = commentRepository.findByCardIdOrderByCreateAtDesc(cardId);
        List<CommentResponse> responses = new ArrayList<>();
        for(Comment comment : comments) {
            CommentResponse commentResponse = CommentResponse.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .build();
            responses.add(commentResponse);
        }
        return responses;
    }
}
