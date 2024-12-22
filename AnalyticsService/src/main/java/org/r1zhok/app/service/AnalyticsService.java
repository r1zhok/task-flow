package org.r1zhok.app.service;

import org.r1zhok.app.controller.response.TaskSummaryResponse;
import org.r1zhok.app.controller.response.TeamProgressResponse;
import org.r1zhok.app.controller.response.UserProgressResponse;

public interface AnalyticsService {

    TaskSummaryResponse taskSummary();

    UserProgressResponse userProgress(String userId);

    TeamProgressResponse teamProgress();
}