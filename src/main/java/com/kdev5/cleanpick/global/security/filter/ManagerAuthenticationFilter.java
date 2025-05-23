package com.kdev5.cleanpick.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AllArgsConstructor
public class ManagerAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ManagerAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationManager authenticationManager1) {
        super(authenticationManager);

        setFilterProcessesUrl("/api/login/manager");
        this.authenticationManager = authenticationManager1;
    }
}
