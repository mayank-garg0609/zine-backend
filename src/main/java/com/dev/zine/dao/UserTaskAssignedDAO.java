package com.dev.zine.dao;

import com.dev.zine.model.UserTaskAssigned;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface UserTaskAssignedDAO extends ListCrudRepository<UserTaskAssigned, Long> {
    Optional<UserTaskAssigned> findById(Long id);
}
