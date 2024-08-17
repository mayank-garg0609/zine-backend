package com.dev.zine.dao;

import com.dev.zine.model.Task;
import com.dev.zine.model.TaskInstance;
import org.springframework.data.repository.ListCrudRepository;
import java.util.List;


public interface TaskInstanceDAO extends ListCrudRepository<TaskInstance, Long> {
    List<TaskInstance> findByTaskId(Task taskId);
}
