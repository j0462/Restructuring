package com.sparta.restructuring.controller;

import com.sparta.restructuring.base.CommonResponse;
import com.sparta.restructuring.dto.CardRequest;
import com.sparta.restructuring.dto.CardResponse;
import com.sparta.restructuring.entity.User;
import com.sparta.restructuring.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.restructuring.base.ControllerUtil.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/card")
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

    @GetMapping("/{status}")
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

    @GetMapping("/{userId}")
    public ResponseEntity<CommonResponse> getCardsByUser(
            @PathVariable Long userId
    ) {
        try{
            User user = new User(); // 인증들어오면 수정
            List<CardResponse> response = cardService.getCardsByUser(user);
            return getResponseEntity(response, "카드 유저별 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    @PostMapping
    public ResponseEntity<CommonResponse> createCard(
            @Valid @RequestBody CardRequest cardRequest,
            BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()){
            return getFieldErrorResponseEntity(bindingResult, "카드 생성 실패");
        }
        try{
            CardResponse response = cardService.createCard(cardRequest);
            return getResponseEntity(response, "카드 생성 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse> updateCard(
            @PathVariable Long id,
            @Valid @RequestBody CardRequest cardRequest,
            BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()){
            return getFieldErrorResponseEntity(bindingResult, "카드 수정 실패");
        }
        try{
            CardResponse response = cardService.updateCard(id, cardRequest);
            return getResponseEntity(response, "카드 수정 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deleteCard(
            @PathVariable Long id
    ) {
        try{
            Long response = cardService.deleteCard(id);
            return getResponseEntity(response, "카드 삭제 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }
}
