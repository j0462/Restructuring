package com.sparta.restructuring.exception.column;

import com.sparta.restructuring.exception.ErrorCode;
import com.sparta.restructuring.exception.GlobalCustomException;

public class InvalidOrderException extends GlobalCustomException {
    public InvalidOrderException(ErrorCode errorCode) {super(errorCode);}
}
