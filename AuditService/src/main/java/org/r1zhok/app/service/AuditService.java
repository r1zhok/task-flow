package org.r1zhok.app.service;

import org.r1zhok.app.controller.response.LogDetailsResponse;
import org.r1zhok.app.controller.response.LogsResponse;

import java.util.List;

public interface AuditService {

    List<LogsResponse> getAllLogs();

    List<LogsResponse> getAllQueryLogs(String query);

    LogDetailsResponse getLogDetails(String id);
}