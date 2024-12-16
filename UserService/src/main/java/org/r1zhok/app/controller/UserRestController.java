package org.r1zhok.app.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.r1zhok.app.controller.payload.UserLoginPayload;
import org.r1zhok.app.controller.payload.UserRegisterPayload;
import org.r1zhok.app.controller.response.AllUsersResponse;
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
    public ResponseEntity<List<AllUsersResponse>> getAllUsers() {
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
        log.info("user is {}", principal.getName());
        return ResponseEntity.ok(userService.getProfile(principal.getName()));
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UserRegisterPayload payload, Principal principal)
            throws UserIdNotFoundException {
        log.info("user update profile {}", principal.getName());
        userService.updateProfile(payload, principal.getName());
        return ResponseEntity.noContent().build();
    }

    /*
    * for admin purposes (assign role to user)
    * */
    @PutMapping("/assign-role/{id}")
    public ResponseEntity<Void> assignRole(@PathVariable String id, @RequestBody List<String> roles) {
        log.info("user assign role {}", id);
        userService.setRole(id, roles);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteProfile(Principal principal) throws UserIdNotFoundException {
        log.info("user delete profile {}", principal.getName());
        userService.deleteUser(principal.getName());
        return ResponseEntity.noContent().build();
    }

    /*
    * for admin purposes (delete user)
    * */
    @DeleteMapping("/profile/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable String id) throws UserIdNotFoundException {
        log.info("admin delete user profile {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}