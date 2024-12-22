package org.r1zhok.app.client;

import de.codecentric.boot.admin.client.registration.BlockingRegistrationClient;
import de.codecentric.boot.admin.client.registration.RegistrationClient;
import org.r1zhok.app.security.OAuthClientHttpRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientBeans {

    @Value("${keycloak.client-id}")
    private String clientId;

    @Bean
    @ConditionalOnProperty(name = "spring.boot.admin.client.enabled", havingValue = "true")
    public RegistrationClient registrationClient(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService
    ) {
        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository,
                        authorizedClientService);

        RestTemplate restTemplate = new RestTemplateBuilder()
                .interceptors((request, body, execution) -> {
                    if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                        OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(OAuth2AuthorizeRequest
                                .withClientRegistrationId("metrics")
                                .principal(clientId)
                                .build());

                        request.getHeaders().setBearerAuth(authorizedClient.getAccessToken().getTokenValue());
                    }

                    return execution.execute(request, body);
                })
                .build();
        return new BlockingRegistrationClient(restTemplate);
    }

    @Bean
    public TaskRestClientImpl booksRestClient(
            @Value("${task.server-url:http://localhost:8082}") String taskBaseUrl,
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository,
            @Value("${task.registration-id:keycloak}") String registrationId,
            LoadBalancerClient loadBalancer
    ) {
        return new TaskRestClientImpl(RestClient.builder()
                .baseUrl(taskBaseUrl)
                .requestInterceptor(new LoadBalancerInterceptor(loadBalancer))
                .requestInterceptor(new OAuthClientHttpRequestInterceptor(
                        new DefaultOAuth2AuthorizedClientManager(
                                clientRegistrationRepository, authorizedClientRepository), registrationId
                ))
                .build());
    }
}