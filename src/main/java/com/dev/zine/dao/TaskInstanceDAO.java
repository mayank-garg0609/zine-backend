package com.dev.zine.dao;

import com.dev.zine.model.TaskInstance;
import org.springframework.data.repository.ListCrudRepository;

public interface TaskInstanceDAO extends ListCrudRepository<TaskInstance, Long> {
}
