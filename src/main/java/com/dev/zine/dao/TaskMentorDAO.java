package com.dev.zine.dao;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.Task;
import com.dev.zine.model.TaskMentor;
import com.dev.zine.model.User;

import java.util.List;

public interface TaskMentorDAO extends ListCrudRepository<TaskMentor, Long>{
    List<TaskMentor> findByTaskId(Task taskId);
    boolean existsByMentorAndTaskId(User user, Task taskId);
}
