package org.r1zhok.app.service;

import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface KeycloakService {

    List<UserRepresentation> getAllUsers();

    UserRepresentation getUser(String id);
}
