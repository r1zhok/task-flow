package org.r1zhok.app.controller.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskPayload(

        @NotBlank(message = "Title is mandatory")
        @Size(min = 3, max = 20, message = "Title must be in range of 3 to 20 characters")
        String title,

        String description,

        String userId
) {}