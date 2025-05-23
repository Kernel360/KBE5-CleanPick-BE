package com.kdev5.cleanpick.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    CUSTOMER("수요자"), MANGER("매니저"), ADMIN("어드민");

    private final String value;
}
