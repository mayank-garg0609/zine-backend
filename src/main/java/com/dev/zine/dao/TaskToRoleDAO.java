package com.dev.zine.dao;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.Role;
import com.dev.zine.model.Task;
import com.dev.zine.model.TaskToRole;
import java.util.List;


public interface TaskToRoleDAO extends ListCrudRepository<TaskToRole, Long> {
    void deleteByTaskIdAndRoleId(Task task, Role role);
    boolean existsByTaskIdAndRoleId(Task task, Role role);
    List<TaskToRole> findByRoleId(Role roleId);
}
