package org.r1zhok.app.mapper;

import lombok.extern.slf4j.Slf4j;
import org.r1zhok.app.controller.response.LogDetailsResponse;
import org.r1zhok.app.controller.response.LogsResponse;
import org.r1zhok.app.entity.LogEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class LogsMapperImpl implements LogsMapper {

    @Override
    public List<LogsResponse> toListLogsConvertor(List<LogEntity> logEntities) {
        return logEntities.stream()
                .map(log -> new LogsResponse(log.getId(), log.getService(), log.getAction()))
                .toList();
    }

    @Override
    public LogDetailsResponse toLogDetailsConvertor(LogEntity logEntity) {
        return new LogDetailsResponse(
                logEntity.getService(),
                logEntity.getAction(),
                logEntity.getDetails(),
                logEntity.getPerformedBy(),
                logEntity.getTimestamp()
        );
    }
}
