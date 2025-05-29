package com.kdev5.cleanpick.global.config;

import com.kdev5.cleanpick.global.security.annotation.CustomerIdArgumentResolver;
import com.kdev5.cleanpick.global.security.annotation.ManagerIdArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final CustomerIdArgumentResolver customerIdArgumentResolver;

    private final ManagerIdArgumentResolver managerIdArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(customerIdArgumentResolver);
        resolvers.add(managerIdArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:5137")
            .allowedMethods("*")
            .allowedHeaders("*")
            .exposedHeaders("Authorization")
            .allowCredentials(true);
    }
}
