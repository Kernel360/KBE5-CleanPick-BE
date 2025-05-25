package com.kdev5.cleanpick.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdev5.cleanpick.global.security.auth.principal.CustomUserDetails;
import com.kdev5.cleanpick.global.security.jwt.JwtParams;
import com.kdev5.cleanpick.global.security.jwt.JwtProcessor;
import com.kdev5.cleanpick.global.security.utils.ResponseUtils;
import com.kdev5.cleanpick.user.service.dto.request.LocalLoginRequestDto;
import com.kdev5.cleanpick.user.service.dto.response.LoginResponseDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(String url, AuthenticationManager authManager) {
        setFilterProcessesUrl(url);
        setAuthenticationManager(authManager);
        this.authenticationManager = authManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // RequestDto 가 같이 왔을 것입니다. 없으면 바로 Exception
        try {

            LocalLoginRequestDto requestDto = objectMapper.readValue(request.getInputStream(), LocalLoginRequestDto.class);


            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword());
            Authentication authenticated = authenticationManager.authenticate(authToken);

            return authenticated;

        } catch (Exception e) {
            // 에러 발생했으니 unsuccessful 이 시스템 상 자동으로 타진다 // 이쪽으로 타서, LoginFailure 쪽 Listener 가 반응한다
            throw new InternalAuthenticationServiceException("인증 중 에러: " + e.getMessage());
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        ResponseUtils.errResponse(response, "로그인 실패", HttpStatus.UNAUTHORIZED);

    }

    // ** return authentication 이 정상적으로 동작하였을 때 수행된다 **
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authResult) throws IOException, ServletException {

        CustomUserDetails loginUser = (CustomUserDetails) authResult.getPrincipal();
        String jwtToken = JwtProcessor.create(loginUser);
        response.addHeader(JwtParams.HEADER, jwtToken);

        LoginResponseDto loginRespDto = new LoginResponseDto(loginUser.getId(), loginUser.getUsername());
        String responseBody = objectMapper.writeValueAsString(loginRespDto);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(responseBody);

    }
}
