package com.dev.zine.dao;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.InstanceCheckpoints;
import com.dev.zine.model.TaskInstance;

public interface InstanceCheckpointsDAO extends ListCrudRepository<InstanceCheckpoints, Long>{
    List<InstanceCheckpoints> findByTaskInstance(TaskInstance taskInstance);
    Long countByTaskInstance(TaskInstance taskInstance);
}
