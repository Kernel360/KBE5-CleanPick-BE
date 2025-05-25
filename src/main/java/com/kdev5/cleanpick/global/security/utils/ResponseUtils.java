package com.kdev5.cleanpick.global.security.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdev5.cleanpick.global.exception.ErrorCode;
import com.kdev5.cleanpick.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

public class ResponseUtils {

    public static void errResponse(HttpServletResponse response, String errMsg, HttpStatus statusCode) {

        /*
         401 - 미인증 및 권한 확인 어려움
         403 - 권한 부족
         404 - 해당 경로에 대한 백엔드 서버가 대응하지 않음
         */

        try {

            response.setContentType("application/json; charset=utf-8"); // JSON 으로 줄것임
            response.setStatus(statusCode.value());
            response.getWriter().println(ApiResponse.error(ErrorCode.UNAUTHENTICATED_USER, errMsg));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
