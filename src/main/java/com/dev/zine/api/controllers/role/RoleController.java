package com.dev.zine.api.controllers.role;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.zine.api.model.role.RoleBody;
import com.dev.zine.api.model.role.RolesListBody;
import com.dev.zine.exceptions.RoleNotFound;
import com.dev.zine.model.Role;
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
    public ResponseEntity<?> createRole(@RequestBody RoleBody body) {
        try{
            Role newRole = roleService.createRole(body);
            return ResponseEntity.ok().body(Map.of("role",newRole));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping()
    public ResponseEntity<?> getAllRoles() {
        try{
            List<Role> roles = roleService.getRoles();
            return ResponseEntity.ok().body(Map.of("roles",roles));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<?> getRole(@PathVariable Long roleId) throws RoleNotFound{
        try{
            Role r = roleService.getRole(roleId);
            return ResponseEntity.ok().body(Map.of("role",r));
        } catch(RoleNotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<?> updateRole(@PathVariable Long roleId, @RequestBody RoleBody body) throws RoleNotFound{
        try{
            Role r = roleService.updateRole(roleId, body);
            return ResponseEntity.ok().body(Map.of("role",r));
        } catch(RoleNotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteRole(@RequestBody RolesListBody body) {
        try{
            roleService.deleteRole(body.getRoleIds());
            return ResponseEntity.ok().build();
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    
    
}
