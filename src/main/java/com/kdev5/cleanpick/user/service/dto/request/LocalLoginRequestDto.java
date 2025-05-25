package com.kdev5.cleanpick.user.service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocalLoginRequestDto {

    @Email
    private final String email;

    @NotEmpty
    private final String password;

}
