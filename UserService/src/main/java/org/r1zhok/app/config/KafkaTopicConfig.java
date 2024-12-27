package org.r1zhok.app.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic createUserTopic() {
        return TopicBuilder.name("createUser").build();
    }

    @Bean
    public NewTopic createLogTopic() {
        return TopicBuilder.name("createLog").build();
    }
}