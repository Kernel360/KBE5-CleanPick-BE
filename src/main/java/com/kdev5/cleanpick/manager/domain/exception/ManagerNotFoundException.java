package com.kdev5.cleanpick.manager.domain.exception;

import com.kdev5.cleanpick.global.exception.BaseException;
import com.kdev5.cleanpick.global.exception.ErrorCode;

public class ManagerNotFoundException extends BaseException {
    public ManagerNotFoundException() {
        super(ErrorCode.MANAGER_NOT_FOUND);
    }
}
