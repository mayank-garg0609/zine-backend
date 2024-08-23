package com.dev.zine.dao;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.TaskInstanceComments;

public interface TaskInstanceCommentsDAO extends ListCrudRepository<TaskInstanceComments, Long>{
    
}
