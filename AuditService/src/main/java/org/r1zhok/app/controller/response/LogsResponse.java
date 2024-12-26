package org.r1zhok.app.controller.response;

import java.util.UUID;

public record LogsResponse(UUID id, String service, String action) {
}