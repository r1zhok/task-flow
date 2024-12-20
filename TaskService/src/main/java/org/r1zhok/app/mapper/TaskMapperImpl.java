package org.r1zhok.app.mapper;

import org.r1zhok.app.controller.payload.TaskPayload;
import org.r1zhok.app.controller.response.TaskDetailResponse;
import org.r1zhok.app.controller.response.TaskResponse;
import org.r1zhok.app.entity.Status;
import org.r1zhok.app.entity.TaskEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public TaskEntity convertPayloadToEntity(TaskPayload payload, String userId) {
        return new TaskEntity(
                null,
                payload.title(),
                payload.description(),
                Status.NEW,
                payload.userId(),
                userId,
                LocalDateTime.now(),
                null
        );
    }

    @Override
    public List<TaskResponse> convertListEntityToListResponse(List<TaskEntity> entity) {
        return entity.stream().map(task -> new TaskResponse(task.getTitle(), task.getDescription())).toList();
    }

    @Override
    public TaskDetailResponse convertEntityToDetailResponse(TaskEntity entity) {
        return new TaskDetailResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getAssigned_to()
        );
    }
}