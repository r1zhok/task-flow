package org.r1zhok.app.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.r1zhok.app.config.KafkaSender;
import org.r1zhok.app.controller.payload.UserLoginPayload;
import org.r1zhok.app.controller.payload.UserRegisterPayload;
import org.r1zhok.app.controller.response.AllUsersResponse;
import org.r1zhok.app.entity.LogEntity;
import org.r1zhok.app.exception.UserAlreadyRegisteredException;
import org.r1zhok.app.exception.UserCreationFailedException;
import org.r1zhok.app.exception.UserIdNotFoundException;
import org.r1zhok.app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    private final KafkaSender kafkaSender;

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
        kafkaSender.sendMessageForAuditService(new LogEntity(
                UUID.randomUUID(),
                "user-service",
                "register",
                Map.of("user", payload),
                payload.email(),
                LocalDateTime.now()
        ), "createLog");
        userService.registerUser(payload);
        return ResponseEntity.created(URI.create("/api/users/login")).build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginPayload payload) {
        kafkaSender.sendMessageForAuditService(new LogEntity(
                UUID.randomUUID(),
                "user-service",
                "login",
                Map.of("user", payload),
                payload.username(),
                LocalDateTime.now()
        ), "createLog");
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
        kafkaSender.sendMessageForAuditService(new LogEntity(
                UUID.randomUUID(),
                "user-service",
                "update-profile",
                Map.of("user", payload),
                payload.username(),
                LocalDateTime.now()
        ), "createLog");
        userService.updateProfile(payload, principal.getName());
        return ResponseEntity.noContent().build();
    }

    /*
     * for admin purposes (assign role to user)
     * */
    @PutMapping("/assign-role/{id}")
    public ResponseEntity<Void> assignRole(@PathVariable String id, @RequestBody List<String> roles, Principal principal) {
        kafkaSender.sendMessageForAuditService(new LogEntity(
                UUID.randomUUID(),
                "user-service",
                "assign-role",
                Map.of("userId", id, "roles", roles),
                principal.getName(),
                LocalDateTime.now()
        ), "createLog");
        userService.setRole(id, roles);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteProfile(Principal principal) throws UserIdNotFoundException {
        kafkaSender.sendMessageForAuditService(new LogEntity(
                UUID.randomUUID(),
                "user-service",
                "delete-profile",
                null,
                principal.getName(),
                LocalDateTime.now()
        ), "createLog");
        userService.deleteUser(principal.getName());
        return ResponseEntity.noContent().build();
    }

    /*
     * for admin purposes (delete user)
     * */
    @DeleteMapping("/profile/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable String id, Principal principal) throws UserIdNotFoundException {
        kafkaSender.sendMessageForAuditService(new LogEntity(
                UUID.randomUUID(),
                "user-service",
                "admin-delete-profile",
                null,
                principal.getName(),
                LocalDateTime.now()
        ), "createLog");
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}