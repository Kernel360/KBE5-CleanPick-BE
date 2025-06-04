package com.kdev5.cleanpick.user.service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public final class SignUpRequestDto {

    @Email(message = "유효한 이메일형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String password;

}
