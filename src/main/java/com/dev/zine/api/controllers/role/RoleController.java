package com.dev.zine.api.controllers.role;

import com.dev.zine.api.model.role.Role;
import com.dev.zine.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private RoleService roleService;

    @PostMapping("/create")
    public ResponseEntity createRoom(@Valid @RequestBody Role role) {
        try {
            roleService.createRole(role);
            return ResponseEntity.ok().body("Role created");
        } catch (Exception ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/get")
    public ResponseEntity getRole(@RequestParam Long id) {
        try{
            roleService.getRole(id);
            return ResponseEntity.ok().body("Role found");
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    @PostMapping("/delete")
    public ResponseEntity deleteRole(@RequestParam List<Long> ids) {
        try{
            roleService.deleteRole(ids);
            return ResponseEntity.ok().body("Role deleted");
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    @PostMapping("/Update")
    public ResponseEntity updateRole(@RequestParam Long id , @RequestBody Role role) {
        try{
            roleService.updateRole(id , role);
            return ResponseEntity.ok().body("Role updated");
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
