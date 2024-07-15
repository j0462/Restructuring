package com.sparta.restructuring.exception.errorCode;

import com.sparta.restructuring.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BoardErrorCode implements ErrorCode {

    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 보드가 존재하지 않습니다."),
    INVALID_BOARD_DATA(HttpStatus.BAD_REQUEST.value(), "보드 이름과 한 줄 설명은 필수입니다."),
    PERMISSION_DENIED(HttpStatus.FORBIDDEN.value(), "권한이 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "사용자가 존재하지 않습니다."),
    USER_ALREADY_INVITED(HttpStatus.BAD_REQUEST.value(), "사용자는 이미 초대되었습니다.");

    private final int httpStatusCode;
    private final String errorDescription;

}
