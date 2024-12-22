package org.r1zhok.app.controller.response;

public record TaskSummaryResponse(
        Integer totalTasks,
        Integer completedTasks,
        Integer newTasks,
        Integer inProgressTasks,
        Integer failedTasks
) {}