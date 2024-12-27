package org.r1zhok.app.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic createTaskTopic() {
        return TopicBuilder.name("createTask").build();
    }

    @Bean
    public NewTopic updateTaskTopic() {
        return TopicBuilder.name("updateTask").build();
    }

    @Bean
    public NewTopic deleteTaskTopic() {
        return TopicBuilder.name("deleteTask").build();
    }

    @Bean
    public NewTopic assignTaskTopic() {
        return TopicBuilder.name("assignTask").build();
    }

    @Bean
    public NewTopic createLogTopic() {
        return TopicBuilder.name("createLog").build();
    }
}