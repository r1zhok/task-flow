package org.r1zhok.app.service;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.r1zhok.app.controller.payload.UserLoginPayload;
import org.r1zhok.app.exception.UserIdNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
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
                .build();
    }

    @Override
    public List<UserRepresentation> getUsers() {
        return keycloak.realm(realm).users().list();
    }

    @Override
    public Integer createUser(UserRepresentation userRepresentation) {
        Response response = keycloak.realm(realm).users().create(userRepresentation);

        if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
            RoleRepresentation userRealmRole = keycloak.realm(realm).roles()
                    .get("user").toRepresentation();
            keycloak.realm(realm).users().get(CreatedResponseUtil.getCreatedId(response))
                    .roles().realmLevel().add(Collections.singletonList(userRealmRole));
            return Response.Status.CREATED.getStatusCode();
        }
        return Response.Status.BAD_REQUEST.getStatusCode();
    }

    @Override
    public String loginUser(UserLoginPayload payload) throws NotAuthorizedException, BadRequestException {
        try (Keycloak keycloak = this.keycloakCredentialBuilder(payload)) {
            return keycloak.tokenManager().getAccessToken().getToken();
        }
    }

    @Override
    public List<RoleRepresentation> getRoles(String userId) {
        return keycloak.realm(realm).users().get(userId).roles()
                .realmLevel().listAll().stream().filter(role -> !role.getName().equals("default-roles-task-flow"))
                .toList();
    }

    @Override
    public void setRoles(String userId, List<String> roles) {
        keycloak.realm(realm).users().get(userId).roles().realmLevel()
                .add(roles.stream().map(role -> keycloak.realm(realm).roles().get(role)
                        .toRepresentation()).toList());
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