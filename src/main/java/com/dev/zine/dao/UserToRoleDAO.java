package com.dev.zine.dao;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.Role;
import com.dev.zine.model.User;
import com.dev.zine.model.UserToRole;
import java.util.List;


public interface UserToRoleDAO extends ListCrudRepository<UserToRole, Long>{
    boolean existsByUserAndRole(User user, Role role);
    List<UserToRole> findByRole(Role role);
    
}
