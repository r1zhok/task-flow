package org.r1zhok.app.service;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.r1zhok.app.config.KafkaSender;
import org.r1zhok.app.controller.payload.TaskPayload;
import org.r1zhok.app.controller.response.TaskDetailResponse;
import org.r1zhok.app.controller.response.TaskResponse;
import org.r1zhok.app.entity.Status;
import org.r1zhok.app.mapper.TaskMapper;
import org.r1zhok.app.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final KafkaSender kafkaSender;

    private final TaskMapper taskMapper;

    private final TaskRepository taskRepository;


    @Override
    @Transactional
    public void createTask(TaskPayload task, String userId) {
        taskRepository.save(taskMapper.convertPayloadToEntity(task, userId));
        kafkaSender.sendMessage("createTask", task.title());
    }

    @Override
    public List<TaskResponse> listTasks() {
        return taskMapper.convertListEntityToListResponse(
                StreamSupport.stream(taskRepository.findAll().spliterator(), false)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public TaskDetailResponse detailTask(Long id) {
        return taskMapper.convertEntityToDetailResponse(taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found"))
        );
    }

    @Override
    @Transactional
    public void updateTask(Long id, TaskPayload task) {
        taskRepository.findById(id)
                .ifPresentOrElse(entity -> {
                            entity.setTitle(task.title());
                            entity.setDescription(task.description());
                            entity.setUpdated_at(LocalDateTime.now());
                            entity.setAssigned_to(task.userId());
                        }, () -> {
                            throw new NoSuchElementException();
                        }
                );
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<TaskResponse> filterTasks(String status) {
        return taskMapper.convertListEntityToListResponse(
                StreamSupport.stream(taskRepository.findAll().spliterator(), false)
                        .filter(taskEntity -> taskEntity.getStatus().toString().equals(status))
                        .collect(Collectors.toList())
        );
    }

    @Override
    @Transactional
    public void assignTask(Long taskId, String userId) {
        taskRepository.findById(taskId).ifPresentOrElse(taskEntity -> {
            taskEntity.setAssigned_to(userId);
        }, () -> {
            throw new NoSuchElementException();
        });
    }

    @Override
    @Transactional
    public void setStatus(Long id, String status) {
        taskRepository.findById(id)
                .ifPresentOrElse(taskEntity -> {
                            taskEntity.setStatus(Status.valueOf(status));
                        }, () -> {
                            throw new NoSuchElementException();
                        }
                );
    }
}