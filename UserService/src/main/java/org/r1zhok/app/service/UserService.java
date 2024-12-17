package org.r1zhok.app.service;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import org.r1zhok.app.controller.payload.UserLoginPayload;
import org.r1zhok.app.controller.payload.UserRegisterPayload;
import org.r1zhok.app.controller.response.AllUsersResponse;
import org.r1zhok.app.controller.response.UserInfoResponse;
import org.r1zhok.app.exception.UserAlreadyRegisteredException;
import org.r1zhok.app.exception.UserCreationFailedException;
import org.r1zhok.app.exception.UserIdNotFoundException;

import java.util.List;

public interface UserService {

    List<AllUsersResponse> getAllUsers();

    void registerUser(UserRegisterPayload payload) throws UserAlreadyRegisteredException, UserCreationFailedException;

    String loginUser(UserLoginPayload payload) throws NotAuthorizedException, BadRequestException;

    UserInfoResponse getProfile(String principalName);

    void updateProfile(UserRegisterPayload payload, String principalName) throws UserIdNotFoundException;

    void setRole(String id, List<String> roles);

    void deleteUser(String id) throws UserIdNotFoundException;
}