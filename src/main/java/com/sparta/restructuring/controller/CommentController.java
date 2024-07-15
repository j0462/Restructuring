package com.sparta.restructuring.controller;

import com.sparta.restructuring.base.CommonResponse;
import com.sparta.restructuring.dto.CardRequest;
import com.sparta.restructuring.dto.CardResponse;
import com.sparta.restructuring.dto.CommentRequest;
import com.sparta.restructuring.dto.CommentResponse;
import com.sparta.restructuring.service.CardService;
import com.sparta.restructuring.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.restructuring.base.ControllerUtil.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/card/{cardId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommonResponse> addComment(
            @PathVariable Long cardId,
            @Valid @RequestBody CommentRequest commentRequest,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "댓글 생성 실패");
        }
        try{
            CommentResponse response = commentService.addComment(commentRequest, cardId);
            return getResponseEntity(response, "댓글 생성 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getCommentsByCardId(
            @PathVariable Long cardId
    ) {
        try{
            List<CommentResponse> response = commentService.getCommentsByCardId(cardId);
            return getResponseEntity(response, "댓글 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }
}
