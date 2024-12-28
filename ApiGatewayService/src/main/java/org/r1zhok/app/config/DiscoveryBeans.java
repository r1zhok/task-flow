package org.r1zhok.app.config;

import http.DefaultEurekaClientHttpRequestFactorySupplier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.eureka.RestTemplateTimeoutProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;

import java.util.List;

@Configuration
public class DiscoveryBeans {

    @Value("${keycloak.client-id}")
    private String clientId;

    @Bean
    public DefaultEurekaClientHttpRequestFactorySupplier defaultEurekaClientHttpRequestFactorySupplier(
            RestTemplateTimeoutProperties restTemplateTimeoutProperties,
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ReactiveOAuth2AuthorizedClientService authorizedClientService
    ) {
        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager oauth2ClientManager =
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, authorizedClientService
                );
        return new DefaultEurekaClientHttpRequestFactorySupplier(restTemplateTimeoutProperties,
                List.of(((httpRequest, entityDetails, httpContext) -> {
                    if (!httpRequest.containsHeader(HttpHeaders.AUTHORIZATION)) {
                        OAuth2AuthorizedClient authorizedClient = oauth2ClientManager
                                .authorize(OAuth2AuthorizeRequest
                                        .withClientRegistrationId("discovery")
                                        .principal(clientId)
                                        .build()
                                ).block();
                        httpRequest.setHeader(HttpHeaders.AUTHORIZATION, "Bearer %s"
                                .formatted(authorizedClient.getAccessToken().getTokenValue()));
                    }
                }))
        );
    }
}
