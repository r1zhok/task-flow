package org.r1zhok.app.mapper;

import lombok.extern.slf4j.Slf4j;
import org.r1zhok.app.controller.response.TaskSummaryResponse;
import org.r1zhok.app.controller.response.UserProgressResponse;
import org.r1zhok.app.entity.Status;
import org.r1zhok.app.entity.TaskEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TaskToAnalyticsMapperImpl implements TaskToAnalyticsMapper {

    @Override
    public TaskSummaryResponse listEntityToSummaryConverter(List<TaskEntity> taskEntities) {
        Map<Status, Long> statusCount = taskEntities.stream()
                .collect(Collectors.groupingBy(TaskEntity::status, Collectors.counting()));

        int totalTasks = taskEntities.size();
        int completedTasks = statusCount.getOrDefault(Status.COMPLETED, 0L).intValue();
        int newTasks = statusCount.getOrDefault(Status.NEW, 0L).intValue();
        int inProgressTasks = statusCount.getOrDefault(Status.IN_PROGRESS, 0L).intValue();
        int failedTasks = statusCount.getOrDefault(Status.FAILED, 0L).intValue();

        return new TaskSummaryResponse(totalTasks, completedTasks, newTasks, inProgressTasks, failedTasks);
    }

    @Override
    public UserProgressResponse listEntityToProgressConverter(List<TaskEntity> taskEntities, String userId) {
        Map<Status, Long> statusCount = taskEntities.stream()
                .filter(task -> {
                    if (task.assigned_to() == null) return false;
                    return task.assigned_to().equals(userId);
                })
                .collect(Collectors.groupingBy(TaskEntity::status, Collectors.counting()));

        int completedTasks = statusCount.getOrDefault(Status.COMPLETED, 0L).intValue();
        int inProgressTasks = statusCount.getOrDefault(Status.IN_PROGRESS, 0L).intValue();
        int failedTasks = statusCount.getOrDefault(Status.FAILED, 0L).intValue();

        return new UserProgressResponse(completedTasks, inProgressTasks, failedTasks);
    }
}