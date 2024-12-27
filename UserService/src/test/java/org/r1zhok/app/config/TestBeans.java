package org.r1zhok.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import static org.mockito.Mockito.mock;

public class TestBeans {

    @Bean
    public JwtDecoder jwtDecoder() {
        return mock();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return mock();
    }
}