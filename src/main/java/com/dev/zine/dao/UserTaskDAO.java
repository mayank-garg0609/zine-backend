package com.dev.zine.dao;

import com.dev.zine.model.UserTask;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface UserTaskDAO extends ListCrudRepository<UserTask, Long> {
    Optional<UserTask> findById(Long id);
}
