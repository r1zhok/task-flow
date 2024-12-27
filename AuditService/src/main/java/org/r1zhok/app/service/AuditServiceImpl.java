package org.r1zhok.app.service;

import lombok.RequiredArgsConstructor;
import org.r1zhok.app.controller.response.LogDetailsResponse;
import org.r1zhok.app.controller.response.LogsResponse;
import org.r1zhok.app.mapper.LogsMapper;
import org.r1zhok.app.repository.LogRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final LogsMapper logsMapper;

    private final LogRepository logRepository;

    private final AuditServiceWithQuery auditServiceWithQuery;

    @Override
    public List<LogsResponse> getAllLogs() {
        return logsMapper.toListLogsConvertor(StreamSupport.stream(logRepository.findAll().spliterator(), false)
                .collect(Collectors.toList()));
    }

    @Override
    public List<LogsResponse> getAllQueryLogs(String query) {
        return logsMapper.toListLogsConvertor(auditServiceWithQuery.processSearch(query));
    }

    @Override
    public LogDetailsResponse getLogDetails(String id) {
        return logsMapper.toLogDetailsConvertor(logRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NoSuchElementException("No log found with id: " + id)));
    }
}
