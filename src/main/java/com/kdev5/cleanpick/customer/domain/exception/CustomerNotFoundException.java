package com.kdev5.cleanpick.customer.domain.exception;

import com.kdev5.cleanpick.global.exception.BaseException;
import com.kdev5.cleanpick.global.exception.ErrorCode;

public class CustomerNotFoundException extends BaseException {
    public CustomerNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
