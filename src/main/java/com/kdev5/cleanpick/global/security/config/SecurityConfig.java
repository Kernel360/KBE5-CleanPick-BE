package com.kdev5.cleanpick.global.security.config;

import com.kdev5.cleanpick.global.security.auth.service.CustomerLoginService;
import com.kdev5.cleanpick.global.security.auth.service.ManagerLoginService;
import com.kdev5.cleanpick.global.security.filter.JwtLoginAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomerLoginService customerLoginService;
    private final ManagerLoginService managerLoginService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtLoginAuthenticationFilter("/api/login/customer", customerAuthenticationManger(customerLoginService)), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtLoginAuthenticationFilter("/api/login/manager", managerAuthenticationManager(managerLoginService)), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request ->
                        request.anyRequest().permitAll())
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        config.setAllowedOriginPatterns(Collections.singletonList("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager customerAuthenticationManger(CustomerLoginService customerLoginService) {
        DaoAuthenticationProvider customerProvider = new DaoAuthenticationProvider();
        customerProvider.setUserDetailsService(customerLoginService);
        customerProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(customerProvider);
    }

    @Bean
    public AuthenticationManager managerAuthenticationManager(ManagerLoginService managerLoginService){
        DaoAuthenticationProvider managerProvider = new DaoAuthenticationProvider();
        managerProvider.setUserDetailsService(managerLoginService);
        managerProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(managerProvider);
    }


}
