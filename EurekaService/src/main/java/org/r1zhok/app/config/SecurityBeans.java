package org.r1zhok.app.config;

import jakarta.annotation.Priority;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityBeans {

    private final JwtAuthConverter jwtAuthConverter;

    @Bean
    @Priority(0)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/eureka/apps", "/eureka/apps/**")
                .authorizeHttpRequests(request -> request
                        .anyRequest().hasAnyAuthority("SCOPE_discovery")
                )
                .oauth2ResourceServer(customizer -> customizer.jwt(Customizer.withDefaults()))
                .sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(CsrfConfigurer::disable)
                .build();
    }

    @Bean
    @Priority(1)
    public SecurityFilterChain mainSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/**")
                .oauth2Client(Customizer.withDefaults())
                .authorizeHttpRequests(request -> request
                        .anyRequest().hasRole("ADMIN")
                )
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)))
                .build();
    }
}