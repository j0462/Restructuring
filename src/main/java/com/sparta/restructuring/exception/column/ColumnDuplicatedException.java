package com.sparta.restructuring.exception.column;

import com.sparta.restructuring.exception.ErrorCode;
import com.sparta.restructuring.exception.GlobalCustomException;

public class ColumnDuplicatedException extends GlobalCustomException {
    public ColumnDuplicatedException(ErrorCode errorCode) {super(errorCode);}
}
