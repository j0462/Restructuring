package com.sparta.restructuring.controller;

import com.sparta.restructuring.base.CommonResponse;
import com.sparta.restructuring.dto.CardRequest;
import com.sparta.restructuring.dto.CardResponse;
import com.sparta.restructuring.entity.User;
import com.sparta.restructuring.security.UserDetailsImpl;
import com.sparta.restructuring.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.restructuring.base.ControllerUtil.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/card")
public class CardController {

    private final CardService cardService;

    @GetMapping
    public ResponseEntity<CommonResponse> getAllCards() {
        try{
            List<CardResponse> response = cardService.getAllCards();
            return getResponseEntity(response, "카드 전체 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<CommonResponse> getCardsByStatus(
            @PathVariable String status
    ) {
        try{
            List<CardResponse> response = cardService.getCardsByStatus(status);
            return getResponseEntity(response, "카드 상태별 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    @GetMapping("/userId/{userId}")
    public ResponseEntity<CommonResponse> getCardsByUser(
            @PathVariable Long userId
    ) {
        try{
            List<CardResponse> response = cardService.getCardsByUser(userId);
            return getResponseEntity(response, "카드 유저별 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    @PostMapping
    public ResponseEntity<CommonResponse> createCard(
            @Valid @RequestBody CardRequest cardRequest,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if(bindingResult.hasErrors()){
            return getFieldErrorResponseEntity(bindingResult, "카드 생성 실패");
        }
        try{
            CardResponse response = cardService.createCard(cardRequest, userDetails.getUser());
            return getResponseEntity(response, "카드 생성 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    // 카드 수정
    @PutMapping("/{cardId}")
    public ResponseEntity<CommonResponse> updateCard(
        @PathVariable Long cardId,
        @RequestBody CardRequest request,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try{
            CardResponse response = cardService.updateCard(cardId, request, userDetails.getUser());
            return getResponseEntity(response, "카드 수정 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    // 카드 삭제
    @DeleteMapping("/{cardId}")
    public ResponseEntity<CommonResponse> deleteCard(
        @PathVariable Long cardId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try{
            Long response = cardService.deleteCard(cardId, userDetails.getUser());
            return getResponseEntity(response, "카드 삭제 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }
}

