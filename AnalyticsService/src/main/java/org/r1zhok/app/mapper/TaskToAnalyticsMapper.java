package org.r1zhok.app.mapper;

import org.r1zhok.app.controller.response.TaskSummaryResponse;
import org.r1zhok.app.controller.response.UserProgressResponse;
import org.r1zhok.app.entity.TaskEntity;

import java.util.List;

public interface TaskToAnalyticsMapper {

    TaskSummaryResponse listEntityToSummaryConverter(List<TaskEntity> taskEntities);

    UserProgressResponse listEntityToProgressConverter(List<TaskEntity> taskEntities, String userId);
}