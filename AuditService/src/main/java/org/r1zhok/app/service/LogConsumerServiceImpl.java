package org.r1zhok.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.r1zhok.app.entity.LogEntity;
import org.r1zhok.app.repository.LogRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableAsync
@RequiredArgsConstructor
public class LogConsumerServiceImpl implements LogConsumerService {

    private final LogRepository logRepository;

    @Override
    @KafkaListener(topics = "createLog", groupId = "log-group")
    public void createLog(LogEntity logEntity) {
        logRepository.save(logEntity);
    }
}