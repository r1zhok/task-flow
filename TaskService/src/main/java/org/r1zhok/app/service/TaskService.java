package org.r1zhok.app.service;

import org.r1zhok.app.controller.payload.TaskPayload;
import org.r1zhok.app.controller.response.TaskDetailResponse;
import org.r1zhok.app.controller.response.TaskResponse;
import org.r1zhok.app.entity.TaskEntity;

import java.util.List;

public interface TaskService {

    void createTask(TaskPayload task, String userId);

    List<TaskResponse> listTasks();

    List<TaskEntity> listTasksForServices();

    TaskDetailResponse detailTask(Long id);

    void updateTask(Long id, TaskPayload task);

    void deleteTask(Long id);

    List<TaskResponse> filterTasks(String status);

    void assignTask(Long taskId, String userId);

    void setStatus(Long id, String status);
}