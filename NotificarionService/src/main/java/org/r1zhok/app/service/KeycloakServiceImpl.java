package org.r1zhok.app.service;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {

    @Value("${keycloak.realm}")
    private String realm;

    private final Keycloak keycloak;

    @Override
    public List<UserRepresentation> getAllUsers() {
        return keycloak.realm(realm).users().list();
    }

    @Override
    public UserRepresentation getUser(String id) {
        return keycloak.realm(realm).users().get(id).toRepresentation();
    }
}
