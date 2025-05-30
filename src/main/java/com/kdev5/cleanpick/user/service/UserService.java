package com.kdev5.cleanpick.user.service;

import com.kdev5.cleanpick.user.domain.Role;
import com.kdev5.cleanpick.user.domain.Status;
import com.kdev5.cleanpick.user.domain.User;
import com.kdev5.cleanpick.user.domain.exception.EmailAlreadyExistsException;
import com.kdev5.cleanpick.user.infra.UserRepository;
import com.kdev5.cleanpick.user.service.dto.request.SignUpRequestDto;
import com.kdev5.cleanpick.user.service.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto customerSignUp(final SignUpRequestDto customerSignUpRequestDto){

        if(userRepository.existsByEmail(customerSignUpRequestDto.getEmail())){
            throw new EmailAlreadyExistsException();
        }

        final User user = userRepository.save(
            User.builder()
                .email(customerSignUpRequestDto.getEmail())
                .password(passwordEncoder.encode(customerSignUpRequestDto.getPassword()))
                .role(Role.CUSTOMER)
                .status(Status.PENDING)
                .build()
        );

        return UserResponseDto.fromEntity(user);
    }

    @Transactional
    public UserResponseDto managerSignup(final SignUpRequestDto managerSignUpRequestDto){

        if(userRepository.existsByEmail(managerSignUpRequestDto.getEmail())){
            throw new EmailAlreadyExistsException();
        }

        final User user = userRepository.save(
            User.builder()
                .email(managerSignUpRequestDto.getEmail())
                .password(passwordEncoder.encode(managerSignUpRequestDto.getPassword()))
                .role(Role.MANAGER)
                .status(Status.PENDING)
                .build()
        );

        return UserResponseDto.fromEntity(user);
    }
}
