package com.dev.zine.service;

import com.dev.zine.api.model.role.Role;
import com.dev.zine.dao.RoleDAO;
import com.dev.zine.model.Roles;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleDAO roleDAO;

    public Roles createRole(Role role) {
        Roles newRole = new Roles();
        newRole.setPermission(role.getPermission());
        roleDAO.save(newRole);

        return newRole;
    }

    public Role getRole(Long id) {
        Roles rol = roleDAO.findById(id).get();
        Role role = new Role();
        BeanUtils.copyProperties(rol,role);
        return role;
    }

    public String deleteRole(List<Long> ids) {
        roleDAO.deleteAllById(ids);
        return "Role deleted";
    }

    public String updateRole(Long id , Role role) {
        Roles existingRole = roleDAO.findById(id).get();
        existingRole.setPermission(role.getPermission());
        roleDAO.save(existingRole);
        return "Role updated";
    }
}
