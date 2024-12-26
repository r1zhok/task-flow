package org.r1zhok.app.service;

import org.r1zhok.app.entity.LogEntity;

public interface LogConsumerService {

    void createLog(LogEntity logEntity);
}