package org.r1zhok.app.controller.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterPayload(
        @NotBlank(message = "Username is mandatory")
        @Size(min = 3, max = 20, message = "Username must be in range of 3 to 20 characters")
        String username,

        @NotBlank(message = "Firstname is mandatory")
        @Size(min = 3, max = 20, message = "Firstname must be in range of 3 to 10 characters")
        String firstname,

        @NotBlank(message = "Lastname is mandatory")
        @Size(min = 3, max = 20, message = "Lastname must be in range of 3 to 10 characters")
        String lastname,

        @Email
        @NotBlank(message = "Email is mandatory")
        @Size(min = 3, max = 20, message = "Email must be in range of 3 to 20 characters")
        String email,

        @NotBlank(message = "Password is mandatory")
        String password
) {}