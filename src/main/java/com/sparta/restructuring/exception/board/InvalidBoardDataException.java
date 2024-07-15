package com.sparta.restructuring.exception.board;

import com.sparta.restructuring.exception.ErrorCode;
import com.sparta.restructuring.exception.GlobalCustomException;

public class InvalidBoardDataException extends GlobalCustomException {
    public InvalidBoardDataException(ErrorCode errorCode) {
        super(errorCode);
    }
}
