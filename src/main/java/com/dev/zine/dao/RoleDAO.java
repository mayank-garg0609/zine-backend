package com.dev.zine.dao;

import com.dev.zine.model.Role;
import org.springframework.data.repository.ListCrudRepository;

public interface RoleDAO extends ListCrudRepository<Role, Long> {
}
