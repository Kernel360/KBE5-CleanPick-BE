package com.kdev5.cleanpick.user.domain.exception;

import com.kdev5.cleanpick.global.exception.BaseException;
import com.kdev5.cleanpick.global.exception.ErrorCode;

public class InvalidUserRoleException extends BaseException {
    public InvalidUserRoleException(ErrorCode errorCode) {
        super(errorCode);
    }
}
