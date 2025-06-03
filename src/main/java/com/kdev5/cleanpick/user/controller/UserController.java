package com.kdev5.cleanpick.user.controller;

import com.kdev5.cleanpick.global.response.ApiResponse;
import com.kdev5.cleanpick.global.security.auth.CustomUserDetails;
import com.kdev5.cleanpick.user.service.UserService;
import com.kdev5.cleanpick.user.service.dto.request.SignUpRequestDto;
import com.kdev5.cleanpick.user.service.dto.response.UserResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "user API", description = "계정 정보를 관리하는 컨트롤러")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup/customer")
    @Operation(summary = "customer 회원가입", description = "타입이 customer 이며 PENDING 상태의 유저가 생성됩니다.")
    private ResponseEntity<ApiResponse<UserResponseDto>> registerCustomer(@Valid @RequestBody SignUpRequestDto customerSignUpRequestDto) {
        UserResponseDto response = userService.customerSignUp(customerSignUpRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok(response));
    }

    @PostMapping("/signup/manager")
    @Operation(summary = "manager 회원가입", description = "타입이 manager 이며 PENDING 상태인 유저가 생성됩니다.")
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
