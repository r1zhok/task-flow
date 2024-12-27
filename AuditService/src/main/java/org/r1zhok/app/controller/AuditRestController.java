package org.r1zhok.app.controller;

import lombok.RequiredArgsConstructor;
import org.r1zhok.app.controller.response.LogDetailsResponse;
import org.r1zhok.app.controller.response.LogsResponse;
import org.r1zhok.app.service.AuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/audit")
public class AuditRestController {

    private final AuditService auditService;

    @GetMapping("/logs")
    public ResponseEntity<List<LogsResponse>> getLogs() {
        return ResponseEntity.ok(auditService.getAllLogs());
    }

    @GetMapping("/logs/search")
    public ResponseEntity<?> getQueryLogs(@RequestBody String query) {
        return ResponseEntity.ok(auditService.getAllQueryLogs(query));
    }

    @GetMapping("/logs/{id}")
    public ResponseEntity<LogDetailsResponse> getLog(@PathVariable String id) {
        return ResponseEntity.ok(auditService.getLogDetails(id));
    }
}