package org.r1zhok.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.r1zhok.app.config.KafkaSender;
import org.r1zhok.app.controller.payload.TaskPayload;
import org.r1zhok.app.controller.response.TaskDetailResponse;
import org.r1zhok.app.controller.response.TaskResponse;
import org.r1zhok.app.entity.LogEntity;
import org.r1zhok.app.entity.TaskEntity;
import org.r1zhok.app.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskRestController {

    private final TaskService taskService;

    private final KafkaSender kafkaSender;

    @PostMapping("/create")
    public ResponseEntity<Void> createTask(@Valid @RequestBody TaskPayload task, Principal principal) {
        kafkaSender.sendMessageForAuditService(new LogEntity(
                UUID.randomUUID(),
                "task-service",
                "createTask",
                Map.of("task", task),
                principal.getName(),
                LocalDate.now()
        ), "createLog");
        taskService.createTask(task, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<TaskResponse>> listTasks() {
        return ResponseEntity.ok(taskService.listTasks());
    }

    @GetMapping("/list-for-service")
    public ResponseEntity<List<TaskEntity>> listTasksForService() {
        return ResponseEntity.ok(taskService.listTasksForServices());
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<TaskDetailResponse> detailTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.detailTask(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @Valid @RequestBody TaskPayload task, Principal principal) {
        kafkaSender.sendMessageForAuditService(new LogEntity(
                UUID.randomUUID(),
                "task-service",
                "updateTask",
                Map.of("task", task),
                principal.getName(),
                LocalDate.now()
        ), "createLog");
        taskService.updateTask(id, task);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id, Principal principal) {
        kafkaSender.sendMessageForAuditService(new LogEntity(
                UUID.randomUUID(),
                "task-service",
                "deleteTask",
                null,
                principal.getName(),
                LocalDate.now()
        ), "createLog");
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter?status={status}")
    public ResponseEntity<List<TaskResponse>> filterTasks(@PathVariable String status) {
        return ResponseEntity.ok(taskService.filterTasks(status));
    }

    @PutMapping("/assign/{taskId}/{userId}")
    public ResponseEntity<?> assignTask(@PathVariable Long taskId, @PathVariable String userId, Principal principal) {
        kafkaSender.sendMessageForAuditService(new LogEntity(
                UUID.randomUUID(),
                "task-service",
                "assignTask",
                Map.of("taskId", taskId, "userId", userId),
                principal.getName(),
                LocalDate.now()
        ), "createLog");
        taskService.assignTask(taskId, userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/set-status/{id}")
    public ResponseEntity<?> setStatus(@PathVariable Long id, @RequestBody String status, Principal principal) {
        kafkaSender.sendMessageForAuditService(new LogEntity(
                UUID.randomUUID(),
                "task-service",
                "setStatus",
                Map.of("taskId", id, "status", status),
                principal.getName(),
                LocalDate.now()
        ), "createLog");
        taskService.setStatus(id, status);
        return ResponseEntity.noContent().build();
    }
}