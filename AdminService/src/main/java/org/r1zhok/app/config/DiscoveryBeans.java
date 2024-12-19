package org.r1zhok.app.config;

import http.DefaultEurekaClientHttpRequestFactorySupplier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.eureka.RestTemplateTimeoutProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import java.util.List;

@Configuration
public class DiscoveryBeans {

    @Value("${keycloak.admin-client-id}")
    private String clientId;

    @Bean
    @ConditionalOnProperty(name = "eureka.client.enabled", havingValue = "true", matchIfMissing = true)
    public DefaultEurekaClientHttpRequestFactorySupplier defaultEurekaClientHttpRequestFactorySupplier(
            RestTemplateTimeoutProperties restTemplateTimeoutProperties,
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService
    ) {
        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository,
                        authorizedClientService);

        return new DefaultEurekaClientHttpRequestFactorySupplier(restTemplateTimeoutProperties, List.of(
                (request, entity, context) -> {
                    if (!request.containsHeader(HttpHeaders.AUTHORIZATION)) {
                        OAuth2AuthorizedClient discovery = authorizedClientManager
                                .authorize(OAuth2AuthorizeRequest
                                .withClientRegistrationId("discovery")
                                .principal(clientId)
                                .build()
                        );

                        request.setHeader(HttpHeaders.AUTHORIZATION,
                                "Bearer %s".formatted(discovery.getAccessToken().getTokenValue())
                        );
                    }
                }
        ));
    }
}
