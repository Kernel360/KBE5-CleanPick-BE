package com.kdev5.cleanpick.contract.domain.exception;

import com.kdev5.cleanpick.global.exception.BaseException;
import com.kdev5.cleanpick.global.exception.ErrorCode;

public class ContractException extends BaseException {
    public ContractException(ErrorCode errorCode) {
        super(errorCode);
    }
}
