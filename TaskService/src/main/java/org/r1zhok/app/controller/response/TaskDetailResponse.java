package org.r1zhok.app.controller.response;

import org.r1zhok.app.entity.Status;

public record TaskDetailResponse(Long id, String title, String description, Status status, String assigned_to) {
}