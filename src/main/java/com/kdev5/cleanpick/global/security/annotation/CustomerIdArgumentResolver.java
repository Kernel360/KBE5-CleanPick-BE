package com.kdev5.cleanpick.global.security.annotation;

import com.kdev5.cleanpick.customer.infra.repository.CustomerRepository;
import com.kdev5.cleanpick.global.security.auth.principal.CustomUserDetails;
import com.kdev5.cleanpick.global.security.exception.CustomerNotFoundException;
import com.kdev5.cleanpick.global.security.exception.UnAuthenticatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class CustomerIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final CustomerRepository customerRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CustomerId.class)
                && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new UnAuthenticatedException();
        }

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Long userId = userDetails.getId();

        if (!customerRepository.existsById(userId)) {
            throw new CustomerNotFoundException();
        }

        return userId;
    }
}
