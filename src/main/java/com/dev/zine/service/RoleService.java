package com.dev.zine.service;

import com.dev.zine.api.model.role.RoleBody;
import com.dev.zine.dao.RoleDAO;
import com.dev.zine.exceptions.RoleNotFound;
import com.dev.zine.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleDAO roleDAO;

    public Role createRole(RoleBody role) {
        Role newRole = new Role();
        newRole.setPermission(role.getPermission());
        roleDAO.save(newRole);
        return newRole;
    }

    public Role getRole(Long id) throws RoleNotFound{
        try{
            Role role = roleDAO.findById(id).orElse(null);
            if(role != null){
                return role;
            } else{
                throw new RoleNotFound();
            }
        } catch(RoleNotFound e){
            throw e;
        }
    }

    public List<Role> getRoles() throws Exception{
        try{
            List<Role> roles = roleDAO.findAll();
            return roles;
        } catch(Exception e) {
            throw new Exception("Error fetching all roles.");
        }
    }

    public void deleteRole(List<Long> ids) {
        roleDAO.deleteAllById(ids);
    }

    public Role updateRole(Long id , RoleBody role) throws RoleNotFound{
        try{
            Role existingRole = roleDAO.findById(id).orElse(null);
            if(existingRole != null){
                existingRole.setPermission(role.getPermission());
                roleDAO.save(existingRole);
                return existingRole;
            } else{
                throw new RoleNotFound();
            }
        } catch(RoleNotFound e){
            throw e;
        }
    }
}
