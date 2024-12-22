package org.r1zhok.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.r1zhok.app.controller.payload.TaskPayload;
import org.r1zhok.app.controller.response.TaskDetailResponse;
import org.r1zhok.app.controller.response.TaskResponse;
import org.r1zhok.app.entity.TaskEntity;
import org.r1zhok.app.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskRestController {

    private final TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<Void> createTask(@Valid @RequestBody TaskPayload task, Principal principal) {
        log.info("Create task: {}", task);
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
        log.info("Detail task: {}", id);
        return ResponseEntity.ok(taskService.detailTask(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @Valid @RequestBody TaskPayload task) {
        log.info("Update task: {}", task);
        taskService.updateTask(id, task);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        log.info("Delete task: {}", id);
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter?status={status}")
    public ResponseEntity<List<TaskResponse>> filterTasks(@PathVariable String status) {
        return ResponseEntity.ok(taskService.filterTasks(status));
    }

    @PutMapping("/assign/{taskId}/{userId}")
    public ResponseEntity<?> assignTask(@PathVariable Long taskId, @PathVariable String userId) {
        log.info("Assign task: {}", taskId);
        taskService.assignTask(taskId, userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/set-status/{id}")
    public ResponseEntity<?> setStatus(@PathVariable Long id, @RequestBody String status) {
        log.info("Set task status: {}", status);
        taskService.setStatus(id, status);
        return ResponseEntity.noContent().build();
    }
}