package com.kdev5.cleanpick.user.service.dto.request;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LocalLoginRequestDto {

    private final String email;

    private final String password;

}
