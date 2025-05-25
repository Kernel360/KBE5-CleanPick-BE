package com.kdev5.cleanpick.manager.domain.exception;

import com.kdev5.cleanpick.global.exception.BaseException;
import com.kdev5.cleanpick.global.exception.ErrorCode;

public class ManagerNotFoundException extends BaseException {
    public ManagerNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
