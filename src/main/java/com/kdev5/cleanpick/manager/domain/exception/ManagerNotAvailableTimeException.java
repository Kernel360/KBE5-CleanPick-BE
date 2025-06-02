package com.kdev5.cleanpick.manager.domain.exception;

import com.kdev5.cleanpick.global.exception.BaseException;
import com.kdev5.cleanpick.global.exception.ErrorCode;

public class ManagerNotAvailableTimeException extends BaseException {
    public ManagerNotAvailableTimeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
