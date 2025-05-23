package com.kdev5.cleanpick.cleaning.domain.exception;

import com.kdev5.cleanpick.global.exception.BaseException;
import com.kdev5.cleanpick.global.exception.ErrorCode;

public class CleaningOptionNotFoundException extends BaseException {
    public CleaningOptionNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
