package com.kdev5.cleanpick.global.security.filter;


import com.kdev5.cleanpick.global.security.auth.principal.CustomUserDetails;
import com.kdev5.cleanpick.global.security.jwt.JwtParams;
import com.kdev5.cleanpick.global.security.jwt.JwtProcessor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter { // 모든 요청 다 이 필터를 거친다


    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (isHeaderVerify(request)) {

            String pureToken = request.getHeader(JwtParams.HEADER).replace(JwtParams.PREFIX, "");
            CustomUserDetails loginUser = JwtProcessor.verify(pureToken); // 이건 그냥 안에 들어있는 id, role 을 가지고 임시 User 를 만든다. 진짠지는 뒤에 Spring 단에서 판단

            // 그 다음에 authentication 에 등록된 유저인지, 그리고 authorization 확인을 위해 권한을 넣는다
            // 사실 유저를 더 사용하려면, loginUser.getId() 를 통해서 가져와야 하는 부분!
            Authentication authentication = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }
        // false 일 시 Authentication 객체가 비어서 가기 때문에, Authentication 필요한 요청으로 들어갈 경우 에러를 뿜어주게 된다 알아서..
        doFilter(request, response, chain);
    }


    // TOKEN 이란게 있는지 부터 본다
    private boolean isHeaderVerify(HttpServletRequest request) {

        String authoHeader = request.getHeader(JwtParams.HEADER);

        if (authoHeader == null || !authoHeader.startsWith(JwtParams.PREFIX)) { // 보낼 때도 Bearer 붙여야함? ㅋㅋㅋㅋ
            return false;
        } else {
            return true;
        }
    }
}