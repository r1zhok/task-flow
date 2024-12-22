package org.r1zhok.app.client;

import lombok.RequiredArgsConstructor;
import org.r1zhok.app.entity.TaskEntity;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

@RequiredArgsConstructor
public class TaskRestClientImpl implements TaskRestClient {

    private static final ParameterizedTypeReference<List<TaskEntity>> TASK_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {
            };

    private final RestClient restClient;

    private static final String BASIC_URI = "/api/tasks";

    @Override
    public List<TaskEntity> findAllTasks() {
        return restClient
                .get()
                .uri(BASIC_URI + "/list-for-service")
                .retrieve()
                .body(TASK_TYPE_REFERENCE);
    }
}