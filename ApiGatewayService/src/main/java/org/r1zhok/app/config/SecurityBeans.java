package org.r1zhok.app.config;

import jakarta.annotation.Priority;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityBeans {

    private final JwtAuthConverter jwtAuthConverter;

    @Bean
    @Priority(0)
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .securityMatcher(ServerWebExchangeMatchers.pathMatchers("/actuator/**"))
                .authorizeExchange(configure -> configure
                        .pathMatchers("/actuator/**").hasAuthority("SCOPE_metrics")
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .oauth2ResourceServer(customizer ->
                        customizer.jwt(Customizer.withDefaults()))
                .oauth2Client(Customizer.withDefaults())
                .build();
    }

    @Bean
    @Priority(1)
    public SecurityWebFilterChain mainSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(configure -> configure
                        .pathMatchers("/api/users/login", "/api/users/register").permitAll()
                        .pathMatchers("/api/users/profile", "/api/tasks/create", "/api/tasks/list-for-service",
                                "/api/tasks/delete/", "/api/tasks/update", "/api/tasks/list",
                                "/api/tasks/detail/", "/api/tasks/set-status/").authenticated()
                        .pathMatchers("/api/users/assign-role/**", "/api/users", "/api/tasks/assign/",
                                "/api/audit/**", "/api/analytics/**", "/admin/**").hasRole("ADMIN")
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .oauth2ResourceServer(customizer ->
                        customizer.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)))
                .oauth2Client(Customizer.withDefaults())
                .build();
    }
}