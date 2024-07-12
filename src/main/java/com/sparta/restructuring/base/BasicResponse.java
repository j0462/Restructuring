package com.sparta.restructuring.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class BasicResponse<T> {
    private final Boolean isSuccess;
    private final int statusCode;
    private final String message;

    // null인 경우 응답 객체에 포함되지 않음
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    public BasicResponse(Boolean isSuccess, int statusCode, String message, T data) {
        this.isSuccess = isSuccess;
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public static <T> BasicResponse<T> of() {
        return new BasicResponse<>(true, 200, "성공했습니다.", null);
    }

    public static <T> BasicResponse<T> of(Integer statusCode) {
        return new BasicResponse<>(true, statusCode, "성공했습니다.", null);
    }

    public static <T> BasicResponse<T> of(String message) {
        return new BasicResponse<>(true, 200, message, null);
    }

    public static <T> BasicResponse<T> of(String message, T data) {
        return new BasicResponse<>(true, 200, message, data);
    }

    public static <T> BasicResponse<T> of(Integer statusCode, String message) {
        return new BasicResponse<>(true, statusCode, message, null);
    }

    public static <T> BasicResponse<T> of(Integer statusCode, String message, T data) {
        return new BasicResponse<>(true, statusCode, message, data);
    }
}