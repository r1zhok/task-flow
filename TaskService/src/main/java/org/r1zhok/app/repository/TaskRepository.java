package org.r1zhok.app.repository;

import org.r1zhok.app.entity.TaskEntity;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<TaskEntity, Long> {}