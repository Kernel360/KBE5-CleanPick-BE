package com.kdev5.cleanpick.global.security.exception;

import com.kdev5.cleanpick.global.exception.BaseException;
import com.kdev5.cleanpick.global.exception.ErrorCode;

public class UnAuthenticatedException extends BaseException {
    public UnAuthenticatedException() {
        super(ErrorCode.UNAUTHENTICATED_USER);
    }
}
