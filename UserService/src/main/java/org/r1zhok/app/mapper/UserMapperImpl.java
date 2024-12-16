package org.r1zhok.app.mapper;

import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.r1zhok.app.controller.payload.UserRegisterPayload;
import org.r1zhok.app.controller.response.AllUsersResponse;
import org.r1zhok.app.controller.response.UserInfoResponse;
import org.r1zhok.app.service.KeycloakServiceImpl;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

    private final KeycloakServiceImpl keycloakService;

    @Override
    public List<AllUsersResponse> toUsers(List<UserRepresentation> payload) {
        return payload.stream().map(element ->
                new AllUsersResponse(
                        element.getId(),
                        element.getUsername(),
                        element.getFirstName(),
                        element.getLastName(),
                        Instant.ofEpochMilli(element.getCreatedTimestamp())
                                .atZone(ZoneId.systemDefault()).toLocalDateTime(),
                        keycloakService.getRoles(element.getId()).toString()
                )
        ).toList();
    }

    @Override
    public UserRepresentation convertToUserRepresentation(UserRegisterPayload payload) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(payload.username());
        user.setFirstName(payload.firstname());
        user.setLastName(payload.lastname());
        user.setEmail(payload.email());
        user.setEnabled(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(payload.password());
        credential.setTemporary(false);
        user.setCredentials(Collections.singletonList(credential));

        return user;
    }

    @Override
    public UserInfoResponse convertToUserInfoResponse(UserRepresentation userRepresentation) {
        return new UserInfoResponse(userRepresentation.getUsername(),
                userRepresentation.getFirstName(),
                userRepresentation.getLastName(),
                Instant.ofEpochMilli(userRepresentation.getCreatedTimestamp())
                        .atZone(ZoneId.systemDefault()).toLocalDateTime(),
                keycloakService.getRoles(userRepresentation.getId()).toString()
        );
    }
}