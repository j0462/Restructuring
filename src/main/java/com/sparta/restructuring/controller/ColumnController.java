package com.sparta.restructuring.controller;

import com.sparta.restructuring.base.BasicResponse;
import com.sparta.restructuring.dto.ColumnCreateRequestDto;
import com.sparta.restructuring.service.ColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/columns")
@RestController
@RequiredArgsConstructor
public class ColumnController {

    private final ColumnService columnService;

    //컬럼 생성
    @PostMapping
    public ResponseEntity<BasicResponse<Void>> createColumn (@RequestBody ColumnCreateRequestDto requestDto) {

        columnService.createColumn(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(BasicResponse.of("컬럼 생성 완료"));
    }

    //컬럼 순서 변경
    @PatchMapping("/{columnId}/{order}")
    public ResponseEntity<BasicResponse<Void>> modifyColumnOrder(@PathVariable Long columnId, @PathVariable Long order) {

        columnService.modifyColumnOrder(columnId, order);

        return ResponseEntity.status(HttpStatus.CREATED).body(BasicResponse.of("컬럼 순서 변경 완료"));
    }

    //컬럼 삭제
    @DeleteMapping("/{columnId}")
    public ResponseEntity<BasicResponse<Void>> deleteColumn(@PathVariable Long columnId) {

        columnService.deleteColumn(columnId);

        return ResponseEntity.status(HttpStatus.CREATED).body(BasicResponse.of("컬럼 삭제 완료"));
    }


}
