package com.kdev5.cleanpick.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdev5.cleanpick.global.exception.ErrorCode;
import com.kdev5.cleanpick.global.response.ApiResponse;
import com.kdev5.cleanpick.global.security.auth.CustomUserDetails;
import com.kdev5.cleanpick.global.security.jwt.JwtParams;
import com.kdev5.cleanpick.global.security.jwt.JwtProcessor;
import com.kdev5.cleanpick.user.service.dto.request.LoginRequestDto;
import com.kdev5.cleanpick.user.service.dto.response.UserResponseDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;


public class JwtLoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager customerAuthenticationManager;
    private final AuthenticationManager managerAuthenticationManager;
    private final ObjectMapper objectMapper;


    public JwtLoginAuthenticationFilter(
            AuthenticationManager cutomerAuthenticationManager,
            AuthenticationManager managerAuthenticationManager,
            ObjectMapper objectMapper
        ) {
        this.customerAuthenticationManager = cutomerAuthenticationManager;
        this.managerAuthenticationManager = managerAuthenticationManager;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {

            LoginRequestDto requestDto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword());

            if(request.getRequestURI().equals("/login/customer")) {
                return customerAuthenticationManager.authenticate(authToken);
            }
            else if (request.getRequestURI().equals("/login/manager")) {
                return managerAuthenticationManager.authenticate(authToken);
            } else{
                throw new AuthenticationServiceException("지원하지 않는 로그인 경로입니다.");
            }

        } catch (Exception e) {
            System.out.println(e);
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String message = failed.getMessage();
        if(failed instanceof BadCredentialsException){
            message = "이메일 또는 비밀번호가 올바르지 않습니다.";
        }

        String body = objectMapper.writeValueAsString(
                ApiResponse.error(ErrorCode.INVALID_CREDENTIALS, message));
        response.getWriter().write(body);
        response.getWriter().flush();
    }

    // ** return authentication 이 정상적으로 동작하였을 때 수행된다 **
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authResult) throws IOException, ServletException {

        CustomUserDetails loginUser = (CustomUserDetails) authResult.getPrincipal();
        String jwtToken = JwtProcessor.create(loginUser);
        response.addHeader(JwtParams.HEADER, jwtToken);

        UserResponseDto loginRespDto = new UserResponseDto(loginUser.getId(), loginUser.getEmail(), loginUser.getUserStatus());
        String responseBody = objectMapper.writeValueAsString(loginRespDto);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(responseBody);

    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return request.getRequestURI().startsWith("/login");
    }
}
