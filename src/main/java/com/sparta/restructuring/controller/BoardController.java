package com.sparta.restructuring.controller;

import com.sparta.restructuring.base.CommonResponse;
import com.sparta.restructuring.dto.BoardRequest;
import com.sparta.restructuring.dto.BoardResponse;
import com.sparta.restructuring.security.UserDetailsImpl;
import com.sparta.restructuring.service.BoardService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static com.sparta.restructuring.base.ControllerUtil.*;

@RequestMapping("/api/board")
@RestController
@AllArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 보드 목록 조회
    @GetMapping
    public ResponseEntity<CommonResponse> getBoard() {
        try{
            List<BoardResponse> response = boardService.getBoard();
            return getResponseEntity(response, "보드 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    //보드 단건 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<CommonResponse> getBoard(
            @PathVariable int boardId
    ) {
        try{
            BoardResponse response = boardService.getBoardOne(boardId);
            return getResponseEntity(response, "보드 단건 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    // 보드 생성
    @PostMapping
    public ResponseEntity<CommonResponse> postBoard(
            @RequestBody BoardRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try{
            BoardResponse response =  boardService.createBoard(request,userDetails.getUser());
            return getResponseEntity(response, "보드 생성 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    // 보드 수정
    @PutMapping("/{boardId}")
    public ResponseEntity<CommonResponse> putBoard(
            @PathVariable Long boardId,
            @RequestBody BoardRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try{
            BoardResponse response = boardService.updateBoard(boardId, request, userDetails.getUser());
            return getResponseEntity(response, "보드 수정 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    // 보드 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<CommonResponse> deleteBoard(
        @PathVariable Long boardId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try{
            Long response = boardService.deleteBoard(boardId, userDetails.getUser());
            return getResponseEntity(response, "보드 삭제 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    // 보드 초대
    @PostMapping("/{boardId}/invite")
    public ResponseEntity<CommonResponse> inviteUserToBoard(
            @PathVariable Long boardId,
            @RequestBody BoardRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try{
            BoardResponse response = boardService.inviteUserToBoard(boardId, request.getInvitedUsers());
            return getResponseEntity(response, "보드 초대 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }
}
