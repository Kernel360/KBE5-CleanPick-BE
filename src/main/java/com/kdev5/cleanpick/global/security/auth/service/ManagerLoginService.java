package com.kdev5.cleanpick.global.security.auth.service;


import com.kdev5.cleanpick.global.security.auth.principal.CustomUserDetails;
import com.kdev5.cleanpick.global.security.exception.UnAuthenticatedException;
import com.kdev5.cleanpick.manager.domain.Manager;
import com.kdev5.cleanpick.manager.infra.repository.ManagerRepository;
import com.kdev5.cleanpick.user.domain.Role;
import com.kdev5.cleanpick.user.domain.User;
import com.kdev5.cleanpick.user.infra.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerLoginService implements UserDetailsService {

    private final UserRepository userRepository;

    private final ManagerRepository managerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));

        if(user.getRole() != Role.CUSTOMER){
            throw new UsernameNotFoundException(username);
        }

        boolean isExist = managerRepository.existsById(user.getId());

        if (!isExist) {
            throw new UnAuthenticatedException();
        }
        return CustomUserDetails.fromEntity(user);
    }
}
