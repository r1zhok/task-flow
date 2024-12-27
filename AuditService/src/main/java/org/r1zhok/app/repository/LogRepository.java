package org.r1zhok.app.repository;

import org.r1zhok.app.entity.LogEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.UUID;

public interface LogRepository extends ElasticsearchRepository<LogEntity, UUID> {
}