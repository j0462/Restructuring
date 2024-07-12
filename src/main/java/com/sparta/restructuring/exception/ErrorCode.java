package com.sparta.restructuring.exception;

public interface ErrorCode {
    int getHttpStatusCode();
    String getErrorDescription();
}