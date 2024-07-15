package com.sparta.restructuring.exception.board;

import com.sparta.restructuring.exception.ErrorCode;
import com.sparta.restructuring.exception.GlobalCustomException;

public class PermissionDeniedException extends GlobalCustomException {
    public PermissionDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
