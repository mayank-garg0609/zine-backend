package com.dev.zine.dao;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.TaskInstance;
import com.dev.zine.model.TaskInstanceComments;

public interface TaskInstanceCommentsDAO extends ListCrudRepository<TaskInstanceComments, Long>{
    List<TaskInstanceComments> findByTaskInstance(TaskInstance taskInstance);
}
