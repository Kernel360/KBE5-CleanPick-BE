package com.kdev5.cleanpick.review.domain.exception;

import com.kdev5.cleanpick.global.exception.BaseException;
import com.kdev5.cleanpick.global.exception.ErrorCode;

public class ReviewException extends BaseException {
    public ReviewException(ErrorCode errorCode) {
        super(errorCode);
    }
}
