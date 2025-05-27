package com.kdev5.cleanpick.contract.domain.exception;

import com.kdev5.cleanpick.global.exception.BaseException;
import com.kdev5.cleanpick.global.exception.ErrorCode;

public class ContractNotFoundException extends BaseException {

    public ContractNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
