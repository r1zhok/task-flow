package org.r1zhok.app.config;

import jakarta.annotation.Priority;
import lombok.RequiredArgsConstructor;
import org.r1zhok.app.client.OAuthHttpHeadersProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthConverter jwtAuthConverter;

    @Bean
    public OAuthHttpHeadersProvider authHttpHeadersProvider(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService
    ) {
        return new OAuthHttpHeadersProvider(
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository,
                        authorizedClientService)
        );
    }

    @Bean
    @Priority(0)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatchers(customizer -> customizer
                        .requestMatchers(HttpMethod.POST, "/instances")
                        .requestMatchers(HttpMethod.DELETE, "/instances/*")
                        .requestMatchers("/actuator/**")
                )
                .oauth2ResourceServer(customizer -> customizer.jwt(Customizer.withDefaults()))
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers("/instances", "/instances/*")
                        .hasAuthority("SCOPE_metrics")
                        .requestMatchers("/actuator/**").hasAnyAuthority("SCOPE_metrics")
                        .anyRequest().denyAll()
                )
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(CsrfConfigurer::disable)
                .build();
    }

    @Bean
    @Priority(1)
    public SecurityFilterChain uiSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .oauth2Client(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)))
                .authorizeHttpRequests(customizer -> customizer.anyRequest().hasRole("ADMIN"))
                .build();
    }
}