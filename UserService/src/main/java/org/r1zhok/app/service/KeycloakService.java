package org.r1zhok.app.service;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import org.keycloak.representations.idm.UserRepresentation;
import org.r1zhok.app.controller.payload.UserLoginPayload;
import org.r1zhok.app.exception.UserIdNotFoundException;

import java.util.List;

public interface KeycloakService {

    List<UserRepresentation> getUsers();

    Integer createUser(UserRepresentation userRepresentation);

    String loginUser(UserLoginPayload payload) throws NotAuthorizedException, BadRequestException;

    UserRepresentation getProfile(String userId);

    UserRepresentation readUserByEmail(String email);

    void updateUser(UserRepresentation userRepresentation, String userId) throws UserIdNotFoundException;

    void deleteUser(String authId) throws UserIdNotFoundException;
}