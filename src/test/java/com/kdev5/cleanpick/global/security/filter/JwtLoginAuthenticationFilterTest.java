package com.kdev5.cleanpick.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdev5.cleanpick.global.security.auth.CustomUserDetails;
import com.kdev5.cleanpick.global.security.jwt.JwtParams;
import com.kdev5.cleanpick.global.security.jwt.JwtProcessor;
import com.kdev5.cleanpick.user.domain.Role;
import com.kdev5.cleanpick.user.service.dto.request.LoginRequestDto;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class JwtLoginAuthenticationFilterTest {

    @InjectMocks
    private JwtLoginAuthenticationFilter filter;

    @Mock
    private AuthenticationManager customerAuthenticationManager;

    @Mock
    private AuthenticationManager managerAuthenticationManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        filter = new JwtLoginAuthenticationFilter(customerAuthenticationManager, managerAuthenticationManager, objectMapper);
    }


    @Test
    void 지원하지_않는_경로일_경우_예외_발생해야_한다() {
        LoginRequestDto dto = new LoginRequestDto("invalid@test.com", "password");
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/login/other");
        request.setContentType(MediaType.APPLICATION_JSON_VALUE);
        request.setContent("{\"email\":\"invalid@test.com\", \"password\":\"password\"}".getBytes());

        MockHttpServletResponse response = new MockHttpServletResponse();

        assertThrows(AuthenticationServiceException.class, () -> {
            filter.attemptAuthentication(request, response);
        });
    }

    @Test
    void 로그인_실패시_401_에러메시지_반환해야_한다() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        AuthenticationException ex = new BadCredentialsException("비밀번호 틀림");

        filter.unsuccessfulAuthentication(request, response, ex);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        String body = response.getContentAsString();
        assertTrue(body.contains("이메일 또는 비밀번호가 올바르지 않습니다."));
    }


}
