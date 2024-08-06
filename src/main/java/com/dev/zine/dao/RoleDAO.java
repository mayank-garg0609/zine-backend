package com.dev.zine.dao;

import com.dev.zine.model.Roles;
import org.springframework.data.repository.ListCrudRepository;

public interface RoleDAO extends ListCrudRepository<Roles, Long> {
}
