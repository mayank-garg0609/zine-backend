package com.dev.zine.dao;

import com.dev.zine.model.Role;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

public interface RoleDAO extends ListCrudRepository<Role, Long> {
    Optional<Role> findByRoleName(String roleName);
}
