package com.kdev5.cleanpick.common.file.exception;

import com.kdev5.cleanpick.global.exception.BaseException;
import com.kdev5.cleanpick.global.exception.ErrorCode;

public class FileTypeException extends BaseException {
	public FileTypeException(ErrorCode errorCode) {
		super(errorCode);
	}
}
