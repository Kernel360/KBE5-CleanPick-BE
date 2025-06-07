package com.kdev5.cleanpick.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {
	PENDING("대기"), ACTIVE("활성");

	private final String value;
}
