package com.sparta.restructuring.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 유저를 찾을 수 없습니다."),
    EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST.value(), "이미 가입된 이메일입니다.");

    private final int httpStatusCode;
    private final String errorDescription;
}
