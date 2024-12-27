package org.r1zhok.app.mapper;

import org.r1zhok.app.controller.response.LogDetailsResponse;
import org.r1zhok.app.controller.response.LogsResponse;
import org.r1zhok.app.entity.LogEntity;

import java.util.List;

public interface LogsMapper {

    List<LogsResponse> toListLogsConvertor(List<LogEntity> logEntities);

    LogDetailsResponse toLogDetailsConvertor(LogEntity logEntity);
}