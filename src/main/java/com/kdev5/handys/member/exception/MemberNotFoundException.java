package com.kdev5.handys.member.exception;

import com.kdev5.handys.global.exception.BaseException;
import com.kdev5.handys.global.exception.ErrorCode;

public class MemberNotFoundException extends BaseException {
    public MemberNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
