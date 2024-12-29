package org.r1zhok.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.r1zhok.app.dto.EmailDetails;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final EmailService emailService;

    private final KeycloakService keycloakService;

    @Override
    @KafkaListener(topics = "createTask", groupId = "group1")
    public void createTask(String message) {
        keycloakService.getAllUsers()
                .forEach(user -> sendNotification(user.getEmail(), "New task created", message));
    }

    @Override
    @KafkaListener(topics = "updateTask", groupId = "group1")
    public void updateTask(String message) {
        keycloakService.getAllUsers()
                .forEach(user -> sendNotification(user.getEmail(), "task updated", message));
    }

    @Override
    @KafkaListener(topics = "deleteTask", groupId = "group1")
    public void deleteTask(String message) {
        keycloakService.getAllUsers()
                .forEach(user -> sendNotification(user.getEmail(), "task deleted", message));
    }

    @Override
    @KafkaListener(topics = "assignTask", groupId = "group1")
    public void assignTask(String userId) {
        sendNotification(keycloakService.getUser(userId).getEmail(), "Task assign to you", "check active tasks");
    }

    @Override
    @KafkaListener(topics = "createUser", groupId = "group1")
    public void createUser(String email) {
        sendNotification(email, "Welcome to TaskFlow", "Create tasks or discover existing");
    }

    @Override
    public void sendNotification(String userEmail, String title, String message) {
        emailService.sendEmail(new EmailDetails(userEmail, title, message));
    }
}