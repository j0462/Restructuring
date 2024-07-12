package com.sparta.restructuring.exception.errorCode;

import com.sparta.restructuring.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ColumnErrorCode implements ErrorCode {

    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 보드가 존재하지 않습니다."),
    COLUMN_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 컬럼이 존재하지 않습니다."),
    COLUMN_ALREADY_REGISTERED_ERROR(HttpStatus.BAD_REQUEST.value(), "중복된 컬럼명입니다."),
    INVALID_ORDER(HttpStatus.BAD_REQUEST.value(), "순서 입력값이 올바르지 않습니다"),
    DELETED_BOARD(HttpStatus.NOT_FOUND.value(), "삭제된 보드입니다.");

    private final int httpStatusCode;
    private final String errorDescription;

}
