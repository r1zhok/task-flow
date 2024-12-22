package org.r1zhok.app.client;

import org.r1zhok.app.entity.TaskEntity;

import java.util.List;

public interface TaskRestClient {

    List<TaskEntity> findAllTasks();
}