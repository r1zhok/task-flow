package org.r1zhok.app.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.r1zhok.app.controller.payload.UserLoginPayload;
import org.r1zhok.app.controller.payload.UserRegisterPayload;
import org.r1zhok.app.controller.response.UserInfoResponse;
import org.r1zhok.app.exception.UserAlreadyRegisteredException;
import org.r1zhok.app.exception.UserCreationFailedException;
import org.r1zhok.app.exception.UserIdNotFoundException;
import org.r1zhok.app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    /*
    *  for admin purposes (get all users and set up roles, etc)
    * */
    @GetMapping()
    public ResponseEntity<List<UserInfoResponse>> getAllUsers(Principal principal) {
        if (principal == null) {
            userIsNotAuthenticatedLog();
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserRegisterPayload payload)
            throws UserCreationFailedException, UserAlreadyRegisteredException {
        log.info("registering user {}", payload);
        userService.registerUser(payload);
        return ResponseEntity.created(URI.create("/api/users/login")).build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginPayload payload) {
        log.info("login user {}", payload);
        return ResponseEntity.ok(userService.loginUser(payload));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Principal principal) {
        if (principal == null) {
            userIsNotAuthenticatedLog();
            return ResponseEntity.badRequest().body("User is not authenticated");
        }
        log.info("user is {}", principal.getName());
        return ResponseEntity.ok(userService.getProfile(principal.getName()));
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UserRegisterPayload payload, Principal principal)
            throws UserIdNotFoundException {
        if (principal == null) {
            userIsNotAuthenticatedLog();
            return ResponseEntity.badRequest().body("User is not authenticated");
        }
        log.info("user update profile {}", principal.getName());
        userService.updateProfile(payload, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getRoles() {
        return null;
    }

    @PutMapping("/assign-role/{id}")
    public ResponseEntity<?> assignRole(@PathVariable long id) {
        return null;
    }

    private void userIsNotAuthenticatedLog() {
        log.info("user is not authenticated");
    }
}