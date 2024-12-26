package org.r1zhok.app.service;

import lombok.RequiredArgsConstructor;
import org.r1zhok.app.entity.LogEntity;
import org.r1zhok.app.repository.LogRepository;
import org.springframework.kafka.annotation.KafkaListener;

@RequiredArgsConstructor
public class LogConsumerServiceImpl implements LogConsumerService {

    private final LogRepository logRepository;

    @Override
    @KafkaListener(topics = "createLog", groupId = "log-group")
    public void createLog(LogEntity logEntity) {
        logRepository.save(logEntity);
    }
}
