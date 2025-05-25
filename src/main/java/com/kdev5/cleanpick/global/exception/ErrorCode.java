package com.kdev5.cleanpick.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    VALIDATION_FAIL(HttpStatus.BAD_REQUEST, "VALIDATION_FAIL", "입력값이 유효하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "유저를 찾을 수 없습니다."),
    CONTRACT_NOT_FOUND(HttpStatus.BAD_REQUEST, "CONTRACT_NOT_FOUND", "계약을 찾을 수 없습니다."),
    REVIEW_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "REVIEW_ALREADY_EXISTS", "이미 작성된 리뷰입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "EMAIL_ALREADY_EXISTS", "이미 가입된 이메일입니다."),
    CUSTOMER_NOT_FOUND(HttpStatus.BAD_REQUEST, "CUSTOMER_NOT_FOUND", "고객을 찾을 수 없습니다."),
    MANAGER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MANAGER_NOT_FOUND", "매니저을 찾을 수 없습니다."),
    UNAUTHENTICATED_USER(HttpStatus.UNAUTHORIZED, "UNAUTHENTICATED_USER", "인증 정보를 찾을 수 없습니다." );

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}