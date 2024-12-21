package org.r1zhok.app.service;

public interface NotificationService {

    void createTask(String message);

    void updateTask(String message);

    void deleteTask(String message);

    void assignTask(String userId);

    void sendNotification(String userEmail, String title, String message);
}
