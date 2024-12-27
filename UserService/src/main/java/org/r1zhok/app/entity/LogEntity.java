package org.r1zhok.app.entity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record LogEntity(
        UUID id,

        String service,

        String action,

        Map<String, Object> details,

        String performedBy,

        LocalDateTime timestamp
) {
}