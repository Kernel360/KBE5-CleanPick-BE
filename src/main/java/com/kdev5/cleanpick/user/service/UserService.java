package com.kdev5.cleanpick.user.service;

import com.kdev5.cleanpick.user.domain.User;
import com.kdev5.cleanpick.user.domain.exception.EmailAlreadyExistsException;
import com.kdev5.cleanpick.user.infra.UserRepository;
import com.kdev5.cleanpick.user.service.dto.request.CustomerSignUpRequestDto;
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
    public UserResponseDto customerSignUp(CustomerSignUpRequestDto customerSignUpRequestDto){

        if(userRepository.existsByEmail(customerSignUpRequestDto.getEmail())){
            throw new EmailAlreadyExistsException();
        }

        User user = userRepository.save(customerSignUpRequestDto.toEntity(passwordEncoder));
        return UserResponseDto.fromEntity(user);
    }
}
