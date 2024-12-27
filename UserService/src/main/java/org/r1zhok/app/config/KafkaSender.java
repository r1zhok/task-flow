package org.r1zhok.app.config;

import lombok.extern.slf4j.Slf4j;
import org.r1zhok.app.entity.LogEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaSender {

    @Qualifier("kafkaTemplateForNotificationService")
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Qualifier("kafkaTemplateForAuditService")
    private final KafkaTemplate<String, LogEntity> kafkaTemplate2;

    public KafkaSender(KafkaTemplate<String, String> kafkaTemplate,
                       KafkaTemplate<String, LogEntity> kafkaTemplate2) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplate2 = kafkaTemplate2;
    }

    public void sendMessageForNotificationService(String message, String topicName) {
        kafkaTemplate.send(topicName, message);
    }

    public void sendMessageForAuditService(LogEntity logEntity, String topicName) {
        kafkaTemplate2.send(topicName, logEntity);
    }
}