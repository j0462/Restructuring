package com.sparta.restructuring.exception;

import lombok.Getter;

@Getter
public class GlobalCustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public GlobalCustomException(ErrorCode errorCode) {
        super(errorCode.getErrorDescription());
        this.errorCode = errorCode;
    }
}
