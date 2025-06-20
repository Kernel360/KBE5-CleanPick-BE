package com.kdev5.cleanpick.user.service.dto.response;

import com.kdev5.cleanpick.user.domain.UserStatus;
import com.kdev5.cleanpick.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserResponseDto {

    private final Long id;

    private final String email;

    private final UserStatus userStatus;

    public static UserResponseDto fromEntity(User user) {

        return new UserResponseDto(user.getId(), user.getEmail(), user.getUserStatus());
    }
}
