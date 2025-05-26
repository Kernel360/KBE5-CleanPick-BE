package com.kdev5.cleanpick.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginType {
    LOCAL("로컬"), GOOGLE("구글");

    private final String value;
}
