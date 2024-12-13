package org.r1zhok.app.service;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.UserRepresentation;
import org.r1zhok.app.controller.payload.UserLoginPayload;
import org.r1zhok.app.controller.payload.UserRegisterPayload;
import org.r1zhok.app.exception.UserIdNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    private final Keycloak keycloak;

    private Keycloak keycloakCredentialBuilder(UserLoginPayload payload) {
        return KeycloakBuilder.builder()
                .realm(this.realm)
                .serverUrl(this.serverUrl)
                .clientId(this.clientId)
                .clientSecret(this.clientSecret)
                .grantType(OAuth2Constants.PASSWORD)
                .username(payload.username())
                .password(payload.password())
                .scope("microprofile-jwt")
                .build();
    }

    @Override
    public List<UserRepresentation> getUsers() {
        return keycloak.realm(realm).toRepresentation().getUsers();
    }

    @Override
    public Integer createUser(UserRepresentation userRepresentation) {
        return keycloak.realm(realm).users().create(userRepresentation).getStatus();
    }

    @Override
    public String loginUser(UserLoginPayload payload) throws NotAuthorizedException, BadRequestException {
        try (Keycloak keycloak = this.keycloakCredentialBuilder(payload)) {
            return keycloak.tokenManager().getAccessToken().getToken();
        }
    }

    @Override
    public UserRepresentation readUserByEmail(String email) {
        return keycloak.realm(realm)
                .users().search(null, null, null, email, null, null)
                .stream()
                .findFirst().orElse(null);
    }

    @Override
    public UserRepresentation getProfile(String userId) {
        log.info(keycloak.realm(realm).clients().get(userId).roles().list().toString());
        return keycloak.realm(realm).users().get(userId).toRepresentation();
    }

    @Override
    public void updateUser(UserRepresentation userRepresentation, String userId) throws UserIdNotFoundException {
        var user = Optional.ofNullable(keycloak.realm(realm)
                .users()
                .get(userId)
        ).orElseThrow(() ->
                new UserIdNotFoundException("User with id " + userId + " not found"));
        user.update(userRepresentation);
    }

    @Override
    public void deleteUser(String authId) throws UserIdNotFoundException {
        var user = Optional.ofNullable(keycloak.realm(realm).users().get(authId))
                .orElseThrow(() -> new UserIdNotFoundException("User with id " + authId + " not found"));
        user.remove();
    }
}