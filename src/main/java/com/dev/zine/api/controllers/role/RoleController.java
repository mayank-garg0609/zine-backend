package com.dev.zine.api.controllers.role;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.zine.api.model.role.RoleAssignBody;
import com.dev.zine.api.model.role.RoleBody;
import com.dev.zine.api.model.role.RolesListBody;
import com.dev.zine.api.model.user.AssignResponse;
import com.dev.zine.api.model.user.UserResponseBody;
import com.dev.zine.exceptions.RoleNotFound;
import com.dev.zine.model.Role;
import com.dev.zine.model.Task;
import com.dev.zine.service.RoleService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    
    @PostMapping()
    // @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> createRole(@RequestBody RoleBody body) {
        try{
            Role newRole = roleService.createRole(body);
            return ResponseEntity.ok().body(Map.of("role",newRole));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping()
    // @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> getAllRoles() {
        try{
            List<Role> roles = roleService.getRoles();
            return ResponseEntity.ok().body(Map.of("roles",roles));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
   
    @GetMapping("/{roleId}")
    // @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> getRole(@PathVariable Long roleId) throws RoleNotFound{
        try{
            // System.out.println(user.getType());
            Role r = roleService.getRole(roleId);
            return ResponseEntity.ok().body(Map.of("role",r));
        } catch(RoleNotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{roleId}")
    // @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> updateRole(@PathVariable Long roleId, @RequestBody RoleBody body) throws RoleNotFound{
        try{
            Role r = roleService.updateRole(roleId, body);
            return ResponseEntity.ok().body(Map.of("role",r));
        } catch(RoleNotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping()
    // @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> deleteRole(@RequestBody RolesListBody body) {
        try{
            roleService.deleteRole(body.getRoleIds());
            return ResponseEntity.ok().build();
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/assign")
    // @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> assignUsers(@RequestBody RoleAssignBody body) throws RoleNotFound{
        try{
            AssignResponse result = roleService.assignRole(body);
            return ResponseEntity.ok().body(result);
        } catch(RoleNotFound e){
            throw e;
        }
    }
    
    @GetMapping("/{roleId}/users")
    // @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> getUsersByRole(@PathVariable Long roleId) throws RoleNotFound{
        try{
            List<UserResponseBody> res = roleService.getUsersByRole(roleId);
            return ResponseEntity.ok().body(Map.of("users", res));
        } catch(RoleNotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/year/{roleName}")
    // @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> addUsersToYear(@PathVariable String roleName) {
        int num = roleService.addUsersToYear(roleName);
        return ResponseEntity.ok().body(Map.of("usersAdded", num));
    }

    @GetMapping("/{roleId}/task")
    // @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> getTaskByRole(@PathVariable Long roleId) {
        try {
            List<Task> tasks = roleService.getTaskByRole(roleId);
            return ResponseEntity.ok().body(Map.of("tasks", tasks));
        } catch(RoleNotFound e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    
}
