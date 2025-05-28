package com.kdev5.cleanpick.global.security.auth;

import com.kdev5.cleanpick.user.domain.Role;
import com.kdev5.cleanpick.user.domain.Status;
import com.kdev5.cleanpick.user.domain.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final Role role;
    private final Status status;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return  Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + role)
        );
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public CustomUserDetails(Long id, String email, String password, Role role, Status status) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public static CustomUserDetails fromEntity(User user){
        return new CustomUserDetails(user.getId(), user.getEmail(), user.getPassword(), user.getRole(), user.getStatus());
    }
}
