package com.sparta.restructuring.controller;

import com.sparta.restructuring.base.BasicResponse;
import com.sparta.restructuring.dto.ColumnCreateRequestDto;
import com.sparta.restructuring.security.UserDetailsImpl;
import com.sparta.restructuring.service.ColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/boards")
@RestController
@RequiredArgsConstructor
public class ColumnController {

    private final ColumnService columnService;

    //컬럼 생성
    @PostMapping("/{boardId}/columns")
    public ResponseEntity<BasicResponse<Void>> createColumn (@RequestBody ColumnCreateRequestDto requestDto, @PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        columnService.createColumn(requestDto, boardId, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(BasicResponse.of("컬럼 생성 완료"));
    }

    //컬럼 순서 변경
    @PatchMapping("/{columnId}/{order}")
    public ResponseEntity<BasicResponse<Void>> modifyColumnOrder(@PathVariable Long columnId, @PathVariable Long order, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        columnService.modifyColumnOrder(columnId, order, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(BasicResponse.of("컬럼 순서 변경 완료"));
    }

    //컬럼 삭제
    @DeleteMapping("/{boardId}{columnId}")
    public ResponseEntity<BasicResponse<Void>> deleteColumn(@PathVariable Long columnId, @PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        columnService.deleteColumn(columnId, boardId, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(BasicResponse.of("컬럼 삭제 완료"));
    }


}