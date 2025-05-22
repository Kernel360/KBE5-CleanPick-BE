package com.kdev5.cleanpick.contract.domain.exception;

import com.kdev5.cleanpick.global.exception.BaseException;
import com.kdev5.cleanpick.global.exception.ErrorCode;

public class ContractNotFoundException extends BaseException {
    public ContractNotFoundException() {
        super(ErrorCode.CONTRACT_NOT_FOUND);
    }
}
