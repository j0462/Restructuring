package com.sparta.restructuring.exception.board;

import com.sparta.restructuring.exception.ErrorCode;
import com.sparta.restructuring.exception.GlobalCustomException;

public class UserNotFoundException extends GlobalCustomException {
    public UserNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
