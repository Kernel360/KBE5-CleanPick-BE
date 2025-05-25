package com.kdev5.cleanpick.global.security.exception;

import com.kdev5.cleanpick.global.exception.BaseException;
import com.kdev5.cleanpick.global.exception.ErrorCode;

public class CustomerNotFoundException extends BaseException {

    public CustomerNotFoundException() {
        super(ErrorCode.CUSTOMER_NOT_FOUND);
    }
}
