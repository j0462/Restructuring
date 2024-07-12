package com.sparta.restructuring.exception.column;

import com.sparta.restructuring.exception.ErrorCode;
import com.sparta.restructuring.exception.GlobalCustomException;

public class ColumnNotFoundException extends GlobalCustomException {
    public ColumnNotFoundException(ErrorCode errorCode) {super(errorCode);}
}
