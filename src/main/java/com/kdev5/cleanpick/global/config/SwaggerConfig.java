package com.kdev5.cleanpick.global.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info =
        @Info(
                title = "cleanPick API",
                version = "v1",
                description = "cleanPick에서 제공하는 API 목록입니다."),
        servers = {
                @Server(url = "http://localhost:8080", description = "로컬 서버 (HTTP)")
        })
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT")
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi groupedOpenApi() {
        String[] paths = {"/**"};

        return GroupedOpenApi.builder().group("clean-pick API").pathsToMatch(paths).build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "bearerAuth",
                                        new io.swagger.v3.oas.models.security.SecurityScheme()
                                                .type(
                                                        io.swagger.v3.oas.models.security
                                                                .SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}