package com.kdev5.cleanpick.cleaning.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceName {
    HOUSE("가정집 청소"),
    OFFICE("사무실 청소"),
    AIRCONDITIONER("에어컨 청소"),
    REFRIGERATOR("냉장고 청소"),
    FURNITURE("가구 청소"),
    HOOD("후드 청소");

    private final String description;
}

