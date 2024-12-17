package org.r1zhok.app.controller.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginPayload(
        @NotBlank(message = "Username is mandatory")
        @Size(min = 3, max = 20, message = "Username must be in range of 3 to 20 characters")
        String username,

        @NotBlank(message = "Password is mandatory")
        String password
) {}
