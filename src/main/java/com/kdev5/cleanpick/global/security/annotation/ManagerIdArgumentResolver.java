package com.kdev5.cleanpick.global.security.annotation;

import com.kdev5.cleanpick.global.security.auth.principal.CustomUserDetails;
import com.kdev5.cleanpick.global.security.exception.CustomerNotFoundException;
import com.kdev5.cleanpick.global.security.exception.UnAuthenticatedException;
import com.kdev5.cleanpick.manager.infra.repository.ManagerRepository;
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
public class ManagerIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final ManagerRepository managerRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ManagerId.class)
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

        if (!managerRepository.existsById(userId)) {
            throw new CustomerNotFoundException();
        }

        return userId;
    }
}