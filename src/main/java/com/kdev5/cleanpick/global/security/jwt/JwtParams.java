package com.kdev5.cleanpick.global.security.jwt;

public abstract class JwtParams {

    public static final String SECRET = "TEMP";
    public static final String HEADER = "Authorization";
    public static final String PREFIX = "Bearer ";
    public static final int EXPIRES_AT = 1000 * 60 * 60 * 24 * 7; // 1주일 임시

}