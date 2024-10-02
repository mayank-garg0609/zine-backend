package com.dev.zine.service;

import com.dev.zine.api.model.role.RoleAssignBody;
import com.dev.zine.api.model.role.RoleBody;
import com.dev.zine.api.model.user.AssignResponse;
import com.dev.zine.api.model.user.UserResponseBody;
import com.dev.zine.dao.RoleDAO;
import com.dev.zine.dao.UserDAO;
import com.dev.zine.dao.UserToRoleDAO;
import com.dev.zine.exceptions.RoleNotFound;
import com.dev.zine.model.Role;
import com.dev.zine.model.User;
import com.dev.zine.model.UserToRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {
    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserToRoleDAO userToRoleDAO;

    public Role createRole(RoleBody role) {
        Role newRole = new Role();
        newRole.setRoleName(role.getRole());
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
                existingRole.setRoleName(role.getRole());
                roleDAO.save(existingRole);
                return existingRole;
            } else{
                throw new RoleNotFound();
            }
        } catch(RoleNotFound e){
            throw e;
        }
    }

    public AssignResponse assignRole(RoleAssignBody body) throws RoleNotFound{
        Role role = roleDAO.findById(body.getRoleId()).orElseThrow(RoleNotFound::new);
        List<UserToRole> list = new ArrayList<>();
        List<String> invalidEmails = new ArrayList<>();
        List<String> alreadyAssignedUser = new ArrayList<>();

        for(String email: body.getUserEmails()){
            User user = userDAO.findByEmailIgnoreCase(email).orElse(null);
            if(user != null && !userToRoleDAO.existsByUserAndRole(user, role)){
                UserToRole userToRole = new UserToRole();
                userToRole.setRole(role);
                userToRole.setUser(user);
                list.add(userToRole);
            } else if(user != null){
                alreadyAssignedUser.add(email);
            } else{
                invalidEmails.add(email);
            }
        }

        if(invalidEmails.isEmpty() && alreadyAssignedUser.isEmpty()){
            userToRoleDAO.saveAll(list);
        } 

        if(invalidEmails.isEmpty() && alreadyAssignedUser.isEmpty()){
            return new AssignResponse("success");
        } else{
            return new AssignResponse("fail", invalidEmails, alreadyAssignedUser);
        }
    }

    public List<UserResponseBody> getUsersByRole(Long roleId) throws RoleNotFound{
        Role role = roleDAO.findById(roleId).orElseThrow(RoleNotFound::new);
        List<UserToRole> userRoles = userToRoleDAO.findByRole(role);
        return userRoles.stream()
                .map(userRole -> {
                    User user = userRole.getUser();
                    UserResponseBody body = new UserResponseBody();
                    body.setEmail(user.getEmail());
                    body.setName(user.getName());
                    return body;
                })
                .collect(Collectors.toList());
    }

    public int addUsersToYear(String roleName) {
        String pattern = roleName + "%@mnit.ac.in";
        Role role =  roleDAO.findByRoleName(roleName).orElse(null);
        int num = 0;

        if(role != null) {
            List<User> users = userDAO.findByEmailMatches(pattern);
            for(User user: users) {
                if(user.isEmailVerified() && !userToRoleDAO.existsByUserAndRole(user, role)) {
                    UserToRole newMapping = new UserToRole();
                    newMapping.setUser(user);
                    newMapping.setRole(role);
                    userToRoleDAO.save(newMapping);
                    num += 1;
                }
            }
        } 

        return num;

    }
}
