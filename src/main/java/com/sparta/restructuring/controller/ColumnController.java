package com.sparta.restructuring.controller;

import static com.sparta.restructuring.base.ControllerUtil.getBadRequestResponseEntity;
import static com.sparta.restructuring.base.ControllerUtil.getResponseEntity;

import java.util.List;

import com.sparta.restructuring.base.BasicResponse;
import com.sparta.restructuring.base.CommonResponse;
import com.sparta.restructuring.dto.CardResponse;
import com.sparta.restructuring.dto.ColumnCreateRequestDto;
import com.sparta.restructuring.dto.ColumnResponse;
import com.sparta.restructuring.security.UserDetailsImpl;
import com.sparta.restructuring.service.ColumnService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.restructuring.base.ControllerUtil.*;


@RequestMapping("/api/boards")
@RestController
@RequiredArgsConstructor
public class ColumnController {

    private final ColumnService columnService;

    //컬럼 생성
    @PostMapping("/{boardId}/columns")
    public ResponseEntity<CommonResponse> createColumn (
            @Valid @RequestBody ColumnCreateRequestDto requestDto,
            BindingResult bindingResult,
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if(bindingResult.hasErrors()){
            return getFieldErrorResponseEntity(bindingResult, "카드 생성 실패");
        }
        try{
            ColumnResponse response = columnService.createColumn(requestDto, boardId, userDetails.getUser());
            return getResponseEntity(response, "컬럼 생성 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }



    //컬럼 순서 변경
    @PatchMapping("/{columnId}/{order}")
    public ResponseEntity<CommonResponse> modifyColumnOrder(
            @PathVariable Long columnId,
            @PathVariable Long order,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try{
            ColumnResponse response = columnService.modifyColumnOrder(columnId, order, userDetails.getUser());
            return getResponseEntity(response, "컬럼 순서 변경 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    //컬럼 삭제
    @DeleteMapping("/{boardId}{columnId}")
    public ResponseEntity<CommonResponse> deleteColumn(
            @PathVariable Long columnId,
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try{
            Long response = columnService.deleteColumn(columnId, boardId, userDetails.getUser());
            return getResponseEntity(response, "컬럼 삭제 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    //컬럼 조회
    @GetMapping("/{boardId}/columns")
    public ResponseEntity<CommonResponse> getAllColumns(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try{
            List<ColumnResponse> response = columnService.getAllColumns(boardId, userDetails.getUser());
            return getResponseEntity(response, "전체 컬럼 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

}