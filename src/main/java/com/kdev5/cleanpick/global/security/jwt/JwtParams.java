package com.kdev5.cleanpick.global.security.jwt;

// TODO
// params VALUE 화 (SECRET)
// refresh 토큰 추가되면 EXPIRES_AT 세부 설정하기
public abstract class JwtParams {

    public static final String SECRET = "TEMP";
    public static final String HEADER = "Authorization";
    public static final String PREFIX = "Bearer ";
    public static final int EXPIRES_AT = 1000 * 60 * 60 * 24 * 7; // 1주일 임시

}