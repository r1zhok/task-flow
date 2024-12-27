package org.r1zhok.app.service;

import lombok.RequiredArgsConstructor;
import org.r1zhok.app.client.TaskRestClient;
import org.r1zhok.app.controller.response.TaskSummaryResponse;
import org.r1zhok.app.controller.response.UserProgressResponse;
import org.r1zhok.app.mapper.TaskToAnalyticsMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "analyticsCache")
public class AnalyticsServiceImpl implements AnalyticsService {

    private final CacheManager cacheManager;

    private final TaskRestClient taskRestClient;

    private final TaskToAnalyticsMapper taskToAnalyticsMapper;

    /*
    * need clear cache if you want to take new data
    * */
    @Override
    @Cacheable(cacheNames = "taskSummary")
    public TaskSummaryResponse taskSummary() {
        waitSomeTime();
        return taskToAnalyticsMapper.listEntityToSummaryConverter(taskRestClient.findAllTasks());
    }

    /*
     * need clear cache if you want to take new data
     * */
    @Override
    @Cacheable(cacheNames = "userProgress", key = "#id", unless = "#result == null")
    public UserProgressResponse userProgress(String id) {
        waitSomeTime();
        return taskToAnalyticsMapper.listEntityToProgressConverter(taskRestClient.findAllTasks(), id);
    }

    @Override
    public void clearCache() {
        cacheManager.getCacheNames()
                .parallelStream()
                .forEach(name -> cacheManager.getCache(name).clear());
    }

    private void waitSomeTime() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}