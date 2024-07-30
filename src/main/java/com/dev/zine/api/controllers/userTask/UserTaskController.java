package com.dev.zine.api.controllers.userTask;

import com.dev.zine.api.model.userTask.UserTask;
import com.dev.zine.service.UserTaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/UserTask")
public class UserTaskController {
    private UserTaskService userTaskSerice;

    public UserTaskController(UserTaskService userTaskSerice) {this.userTaskSerice = userTaskSerice;}

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody UserTask userTask) {
        try{
            userTaskService.createUserTask(userTask);
            return  ResponseEntity.ok().body("User Task Created");
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    @PostMapping("/delete")
    public ResponseEntity delete(@RequestBody List<Long> ids) {
        try {
            userTaskSerice.deleteUserTask(ids);
            return ResponseEntity.ok().body("User Task Deleted " + ids.toString());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/update")
    public ResponseEntity update(@RequestParam Long id, @RequestBody UserTask userTask) {
        try{
            userTaskSerice.updateUserTask(id,userTask);
            return ResponseEntity.ok().body("User Task Updated");
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    @GetMapping("/get")
    public ResponseEntity get(@RequestParam Long id) {
        try{

        }catch (Exception ex){
            
        }
    }
}
