package org.r1zhok.app.mapper;

import org.r1zhok.app.controller.payload.TaskPayload;
import org.r1zhok.app.controller.response.TaskDetailResponse;
import org.r1zhok.app.controller.response.TaskResponse;
import org.r1zhok.app.entity.TaskEntity;

import java.util.List;

public interface TaskMapper {

    TaskEntity convertPayloadToEntity(TaskPayload payload, String userId);

    List<TaskResponse> convertListEntityToListResponse(List<TaskEntity> entity);

    TaskDetailResponse convertEntityToDetailResponse(TaskEntity entity);
}