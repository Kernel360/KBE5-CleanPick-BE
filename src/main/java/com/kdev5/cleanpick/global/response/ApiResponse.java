package com.kdev5.cleanpick.global.response;


import com.kdev5.cleanpick.global.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    private final boolean isSuccess;
    private final T data;
    private final String code;
    private final String message;

    // 요청 성공한 경우
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, "SUCCESS", "요청에 성공하였습니다.");
    }

    // 요청 성공한 경우
    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(true, null, "SUCCESS", "요청에 성공하였습니다.");
    }

    //단순 에러
    public static ApiResponse<?> error(ErrorCode errorCode) {
        return new ApiResponse<>(false, null, errorCode.getCode(), errorCode.getMessage());
    }

    //에러 + 데이터
    public static <T> ApiResponse<T> error(ErrorCode errorCode, T data) {
        return new ApiResponse<>(false, data, errorCode.getCode(), errorCode.getMessage());
    }

}