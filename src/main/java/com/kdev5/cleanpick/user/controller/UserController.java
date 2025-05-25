package com.kdev5.cleanpick.user.controller;

import com.kdev5.cleanpick.global.response.ApiResponse;
import com.kdev5.cleanpick.user.service.UserService;
import com.kdev5.cleanpick.user.service.dto.request.CustomerSignUpRequestDto;
import com.kdev5.cleanpick.user.service.dto.response.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    private ResponseEntity<ApiResponse<UserResponseDto>> registerUser(@Valid @RequestBody CustomerSignUpRequestDto customerSignUpRequestDto) {
        UserResponseDto response = userService.customerSignUp(customerSignUpRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok(response));
    }
}
