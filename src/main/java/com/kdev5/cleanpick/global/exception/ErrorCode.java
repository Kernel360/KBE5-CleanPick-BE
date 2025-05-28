package com.kdev5.cleanpick.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    VALIDATION_FAIL(HttpStatus.BAD_REQUEST, "VALIDATION_FAIL", "입력값이 유효하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "유저를 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "EMAIL_ALREADY_EXISTS", "이미 가입된 이메일입니다."),
    CONTRACT_NOT_FOUND(HttpStatus.BAD_REQUEST, "CONTRACT_NOT_FOUND", "계약을 찾을 수 없습니다."),
    CONTRACT_BAD_REQUEST(HttpStatus.BAD_REQUEST, "CONTRACT_BAD_REQUEST", "1:1 계약이 아닙니다."),
    MANAGER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MANAGER_NOT_FOUND", "존재하지 않는 매니저입니다."),
    CLEANING_NOT_FOUND(HttpStatus.BAD_REQUEST, "CLEANING_NOT_FOUND", "존재하지 않는 청소입니다."),
    CUSTOMER_NOT_FOUND(HttpStatus.BAD_REQUEST, "CUSTOMER_NOT_FOUND", "존재하지 않는 고객입니다."),
    REVIEW_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "REVIEW_ALREADY_EXISTS", "존재하지 않는 리뷰입니다."),
    CLEANING_OPTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "CLEANING_OPTION_NOT_FOUND", "존재하지 않는 청소 요구사항입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "입력 정보가 잘못되었습니다."),
    MATCHING_NOMINEE_NOT_FOUND(HttpStatus.BAD_REQUEST, "MATCHING_NOMINEE_NOT_FOUND", "존재하지 않는 매칭 요청입니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}