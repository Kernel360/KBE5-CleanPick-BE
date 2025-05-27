package com.kdev5.cleanpick.global.security.filter;


import com.kdev5.cleanpick.global.security.auth.CustomUserDetails;
import com.kdev5.cleanpick.global.security.jwt.JwtParams;
import com.kdev5.cleanpick.global.security.jwt.JwtProcessor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (isHeaderVerify(request)) {

            String pureToken = request.getHeader(JwtParams.HEADER).replace(JwtParams.PREFIX, "");
            CustomUserDetails loginUser = JwtProcessor.verify(pureToken);

            Authentication authentication = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }
        doFilter(request, response, chain);
    }

    private boolean isHeaderVerify(HttpServletRequest request) {

        String authHeader = request.getHeader(JwtParams.HEADER);

        return authHeader != null && authHeader.startsWith(JwtParams.PREFIX);
    }
}