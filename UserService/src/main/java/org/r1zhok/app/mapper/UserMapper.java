package org.r1zhok.app.mapper;

import org.keycloak.representations.idm.UserRepresentation;
import org.r1zhok.app.controller.payload.UserRegisterPayload;
import org.r1zhok.app.controller.response.AllUsersResponse;
import org.r1zhok.app.controller.response.UserInfoResponse;

import java.util.List;

public interface UserMapper {

    List<AllUsersResponse> toUsers(List<UserRepresentation> payload);

    UserRepresentation convertToUserRepresentation(UserRegisterPayload payload);

    UserInfoResponse convertToUserInfoResponse(UserRepresentation userRepresentation);
}
