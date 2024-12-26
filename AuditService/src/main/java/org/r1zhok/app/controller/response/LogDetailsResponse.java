package org.r1zhok.app.controller.response;

import java.time.LocalDateTime;
import java.util.Map;

public record LogDetailsResponse(
        String service,
        String action,
        Map<String, Object> details,
        String performedBy,
        LocalDateTime timestamp
) {
}