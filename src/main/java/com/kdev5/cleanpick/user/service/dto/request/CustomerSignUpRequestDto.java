package com.kdev5.cleanpick.user.service.dto.request;

import com.kdev5.cleanpick.user.domain.LoginType;
import com.kdev5.cleanpick.user.domain.Role;
import com.kdev5.cleanpick.user.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
public final class CustomerSignUpRequestDto {

    @Email(message = "유효한 이메일형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String password;

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.CUSTOMER)
                .loginType(LoginType.LOCAL)
                .build();
    }
}
