package com.kdev5.cleanpick.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdev5.cleanpick.user.domain.Role;
import com.kdev5.cleanpick.user.domain.User;
import com.kdev5.cleanpick.user.infra.UserRepository;
import com.kdev5.cleanpick.user.service.dto.request.LoginRequestDto;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.test.web.servlet.MockMvc;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setupUser() {
        userRepository.deleteAll();
        // 테스트용 유저 저장 (JWT 발급을 위해 인증 성공하게)
        userRepository.save(User.builder()
                .email("test@user.com")
                .password(passwordEncoder.encode("1234"))
                .role(Role.MANAGER)
                .build());
    }

    @Test
    void 로그인_성공시_JWT_헤더와_사용자정보_반환() throws Exception {
        // given
        LoginRequestDto request = new LoginRequestDto("test@user.com", "1234");
        String json = objectMapper.writeValueAsString(request);

        // when then
        mockMvc.perform(post("/login/manager")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(jsonPath("$.email").value("test@user.com"));
    }

    @Test
    void 로그인_실패시_401_반환() throws Exception {
        LoginRequestDto request = new LoginRequestDto("test@user.com", "wrong-password");
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/login/manager")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }
}
