package com.dev.zine.dao;

import com.dev.zine.model.TaskInstance;
import com.dev.zine.model.User;
import com.dev.zine.model.UserTaskAssigned;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;


public interface UserTaskAssignedDAO extends ListCrudRepository<UserTaskAssigned, Long> {
    Optional<UserTaskAssigned> findById(Long id);
    List<UserTaskAssigned> findByTaskInstanceId(TaskInstance taskInstanceId);
    boolean existsByTaskInstanceIdAndUserId(TaskInstance instance, User user);
    List<UserTaskAssigned> findByUserId(User userId);
}
