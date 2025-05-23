package com.kdev5.cleanpick.review.domain.exception;

import com.kdev5.cleanpick.global.exception.BaseException;
import com.kdev5.cleanpick.global.exception.ErrorCode;

public class ReviewDuplicateException extends BaseException {
    public ReviewDuplicateException() {
        super(ErrorCode.REVIEW_ALREADY_EXISTS);
    }
}
