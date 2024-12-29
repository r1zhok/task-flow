package org.r1zhok.app.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerCustomConfig {

    @Bean
    GroupedOpenApi publicOpenApi() {
        return GroupedOpenApi.builder()
                .group("public-apis")
                .pathsToMatch("/api/users/login", "/api/users/register")
                .build();
    }

    @Bean
    GroupedOpenApi protectedOpenApi() {
        return GroupedOpenApi.builder()
                .group("protected-apis")
                .pathsToMatch("/api/users", "/api/users/", "/api/users/profile", "/api/users/assign-role/**")
                .build();
    }

    @Bean
    OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info().title("API Documentation").version("1.0"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(
                        new Components()
                                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                )
                );
    }
}
