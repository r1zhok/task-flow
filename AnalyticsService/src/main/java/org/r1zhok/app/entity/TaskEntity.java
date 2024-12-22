package org.r1zhok.app.entity;

import java.time.LocalDateTime;

public record TaskEntity(
        Long id,

        String title,

        String description,

        Status status,

        String assigned_to,

        String created_by,

        LocalDateTime created_on,

        LocalDateTime updated_at
) {}