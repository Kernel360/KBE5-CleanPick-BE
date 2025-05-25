package com.kdev5.cleanpick.global.security.auth.principal;

import com.kdev5.cleanpick.customer.domain.Customer;
import com.kdev5.cleanpick.user.domain.Role;
import com.kdev5.cleanpick.user.domain.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final Role role;

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

    private CustomUserDetails(Long id, String email, String password, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static CustomUserDetails fromEntity(User user){
        return new CustomUserDetails(user.getId(), user.getEmail(), user.getPassword(), user.getRole());
    }
}
