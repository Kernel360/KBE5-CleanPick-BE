package com.kdev5.cleanpick.contract.domain.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchingStatus {
    ACCEPT("요쳥"),
    REJECT("거절"),
    PENDING("대기"),
    CONFIRMED("확정");

    private final String description;
}
