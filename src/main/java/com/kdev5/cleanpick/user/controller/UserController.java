package com.kdev5.cleanpick.user.controller;

import com.kdev5.cleanpick.global.response.ApiResponse;
import com.kdev5.cleanpick.global.security.auth.CustomUserDetails;
import com.kdev5.cleanpick.user.service.UserService;
import com.kdev5.cleanpick.user.service.dto.request.SignUpRequestDto;
import com.kdev5.cleanpick.user.service.dto.response.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup/customer")
    private ResponseEntity<ApiResponse<UserResponseDto>> registerCustomer(@Valid @RequestBody SignUpRequestDto customerSignUpRequestDto) {
        UserResponseDto response = userService.customerSignUp(customerSignUpRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok(response));
    }

    @PostMapping("/signup/manager")
    private ResponseEntity<ApiResponse<UserResponseDto>> registerManager(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        UserResponseDto response = userService.managerSignup(signUpRequestDto);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.ok(response));
    }

    @PostMapping("/logout")
    private ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        userService.logout(customUserDetails.getId());
        return ResponseEntity.ok(ApiResponse.ok());
    }

}
