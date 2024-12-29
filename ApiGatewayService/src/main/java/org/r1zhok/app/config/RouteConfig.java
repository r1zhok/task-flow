package org.r1zhok.app.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    RouteLocator routes(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route("user-service", route -> route.path("/api/users/**")
                        .uri("lb://USER-SERVICE")
                )
                .route("task-service", route -> route.path("/api/tasks/**")
                        .uri("lb://TASK-SERVICE")
                )
                .route("audit-service", route -> route.path("/api/audit/**")
                        .uri("lb://AUDIT-SERVICE")
                )
                .route("analytics-service", route -> route.path("/api/analytics/**")
                        .uri("lb://ANALYTICS-SERVICE")
                )
                .route("admin-service", route -> route.path("/admin/**")
                        .uri("lb://ADMIN-SERVICE")
                )
                .build();
    }
}