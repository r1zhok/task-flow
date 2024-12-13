package org.r1zhok.app.service;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.r1zhok.app.controller.payload.UserLoginPayload;
import org.r1zhok.app.controller.payload.UserRegisterPayload;
import org.r1zhok.app.controller.response.UserInfoResponse;
import org.r1zhok.app.exception.UserAlreadyRegisteredException;
import org.r1zhok.app.exception.UserCreationFailedException;
import org.r1zhok.app.exception.UserIdNotFoundException;
import org.r1zhok.app.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper mapper;

    private final KeycloakService keycloakService;

    @Override
    public List<UserInfoResponse> getAllUsers() {
        return mapper.toUsers(keycloakService.getUsers());
    }

    @Override
    public void registerUser(UserRegisterPayload payload) throws UserAlreadyRegisteredException, UserCreationFailedException {
        UserRepresentation userRepresentation = keycloakService.readUserByEmail(payload.email());
        if (userRepresentation != null) {
            log.error("User with email {} already exists", payload.email());
            throw new UserAlreadyRegisteredException("This email already registered as a user. Please check and retry.");
        }

        if (!(keycloakService.createUser(mapper.convertToUserRepresentation(payload)) == 201)) {
            log.error("User creation failed");
            throw new UserCreationFailedException("Something wrong, please try later");
        }
        log.info("User created");
    }

    @Override
    public String loginUser(UserLoginPayload payload) throws NotAuthorizedException, BadRequestException {
        return keycloakService.loginUser(payload);
    }

    @Override
    public UserInfoResponse getProfile(String principalName) {
        return mapper.convertToUserInfoResponse(keycloakService.getProfile(principalName));
    }

    @Override
    public void updateProfile(UserRegisterPayload payload, String principalName) throws UserIdNotFoundException {
        keycloakService.updateUser(mapper.convertToUserRepresentation(payload), principalName);
    }

    @Override
    public List<String> getRoles() {
        return null;
    }

    @Override
    public void setRole() {}
}