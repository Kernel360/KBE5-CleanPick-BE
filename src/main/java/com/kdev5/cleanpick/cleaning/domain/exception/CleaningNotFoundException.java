package com.kdev5.cleanpick.cleaning.domain.exception;

import com.kdev5.cleanpick.global.exception.BaseException;
import com.kdev5.cleanpick.global.exception.ErrorCode;

public class CleaningNotFoundException extends BaseException {
    public CleaningNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
