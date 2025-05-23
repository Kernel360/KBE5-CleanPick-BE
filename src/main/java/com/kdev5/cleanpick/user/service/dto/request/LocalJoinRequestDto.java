package com.kdev5.cleanpick.user.service.dto.request;

import com.kdev5.cleanpick.user.domain.LoginType;
import com.kdev5.cleanpick.user.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class LocalJoinRequestDto {

    @Email
    private String email;

    @NotEmpty
    private String password;

}
