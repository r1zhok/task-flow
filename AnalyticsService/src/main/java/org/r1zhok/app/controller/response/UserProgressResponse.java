package org.r1zhok.app.controller.response;

public record UserProgressResponse(
        Integer tasksCompleted,
        Integer tasksInProgress,
        Integer tasksFailed
) {}