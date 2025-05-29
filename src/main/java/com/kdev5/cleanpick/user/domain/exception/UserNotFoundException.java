package com.kdev5.cleanpick.user.domain.exception;

import com.kdev5.cleanpick.global.exception.BaseException;
import com.kdev5.cleanpick.global.exception.ErrorCode;

public class UserNotFoundException extends BaseException {
	public UserNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
