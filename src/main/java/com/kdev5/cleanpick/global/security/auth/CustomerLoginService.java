package com.kdev5.cleanpick.global.security.auth;

import com.kdev5.cleanpick.user.domain.Role;
import com.kdev5.cleanpick.user.domain.User;
import com.kdev5.cleanpick.user.infra.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerLoginService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다 (email: " + username +")"));

        if (user.getRole() != Role.CUSTOMER) {
            throw new AccessDeniedException("고객으로 등록된 계정이 아닙니다.");
        }

        return CustomUserDetails.fromEntity(user);
    }
}
