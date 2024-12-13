package org.r1zhok.app.mapper;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.r1zhok.app.controller.payload.UserRegisterPayload;
import org.r1zhok.app.controller.response.UserInfoResponse;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public List<UserInfoResponse> toUsers(List<UserRepresentation> payload) {
        return payload.stream().map(element ->
                new UserInfoResponse(
                        element.getUsername(),
                        element.getFirstName(),
                        element.getLastName(),
                        Instant.ofEpochMilli(element.getCreatedTimestamp())
                                .atZone(ZoneId.systemDefault()).toLocalDateTime()
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
                        .atZone(ZoneId.systemDefault()).toLocalDateTime()
        );
    }
}