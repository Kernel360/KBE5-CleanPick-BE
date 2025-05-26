package com.kdev5.cleanpick.global.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kdev5.cleanpick.global.security.auth.CustomUserDetails;
import com.kdev5.cleanpick.user.domain.Role;
import com.kdev5.cleanpick.user.domain.User;

import java.util.Date;

public class JwtProcessor {


    public static String create(CustomUserDetails loginUser) {
        String jwtToken = JWT.create()
                .withSubject("CleanPick")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtParams.EXPIRES_AT))
                .withClaim("id", loginUser.getId())
                .withClaim("role", loginUser.getRole() + "")
                .sign(Algorithm.HMAC512(JwtParams.SECRET));
        return JwtParams.PREFIX + jwtToken;
    }

    public static CustomUserDetails verify(String token) {

        DecodedJWT decoded = JWT.require(Algorithm.HMAC512(JwtParams.SECRET)).build().verify(token);

        Long id = decoded.getClaim("id").asLong();
        String role = decoded.getClaim("role").asString();

        User onlyForAuthentication = User.builder().id(id).role(Role.valueOf(role)).build();

        return CustomUserDetails.fromEntity(onlyForAuthentication);
    }
}