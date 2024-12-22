package org.r1zhok.app.controller;

import lombok.RequiredArgsConstructor;
import org.r1zhok.app.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analytics")
public class AnalyticsRestController {

    private final AnalyticsService analyticsService;

    @GetMapping("/task-summary")
    public ResponseEntity<?> taskSummary() {
        return ResponseEntity.ok(analyticsService.taskSummary());
    }

    @GetMapping("/user-performance/{id}")
    public ResponseEntity<?> userPerformance(@PathVariable("id") String id) {
        return ResponseEntity.ok(analyticsService.userProgress(id));
    }

    @GetMapping("/team-performance")
    public ResponseEntity<?> teamPerformance() {
        return null;
    }

    @GetMapping("/cache-clear")
    public ResponseEntity<Void> cacheClear() {
        return null;
    }
}