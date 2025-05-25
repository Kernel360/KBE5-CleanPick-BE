package com.kdev5.cleanpick.global.security.auth.principal;

import com.kdev5.cleanpick.customer.domain.Customer;
import com.kdev5.cleanpick.user.domain.Role;
import com.kdev5.cleanpick.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class CustomerAuthenticationPrincipal implements UserDetails {

    private final Long userId;
    private final Long customerId;
    private final String email;
    private final String password;
    private final Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> "ROLE_" + role);
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private CustomerAuthenticationPrincipal(Long userId, Long customerId, String email, String password, Role role) {
        this.userId = userId;
        this.customerId = customerId;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static CustomerAuthenticationPrincipal fromEntity(User user, Customer customer){
        return new CustomerAuthenticationPrincipal(user.getId(), customer.getId(), user.getEmail(), user.getPassword(), user.getRole());
    }
}
