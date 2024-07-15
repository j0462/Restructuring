package com.sparta.restructuring.exception.board;

import com.sparta.restructuring.exception.ErrorCode;
import com.sparta.restructuring.exception.GlobalCustomException;

public class BoardNotFoundException extends GlobalCustomException {
    public BoardNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
