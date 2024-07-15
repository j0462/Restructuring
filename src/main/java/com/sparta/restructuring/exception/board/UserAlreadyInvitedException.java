package com.sparta.restructuring.exception.board;

import com.sparta.restructuring.exception.ErrorCode;
import com.sparta.restructuring.exception.GlobalCustomException;

public class UserAlreadyInvitedException extends GlobalCustomException {
    public UserAlreadyInvitedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
