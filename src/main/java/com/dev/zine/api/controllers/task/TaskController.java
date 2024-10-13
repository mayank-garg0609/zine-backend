package com.dev.zine.api.controllers.task;

import com.dev.zine.api.model.task.InstanceListBody;
import com.dev.zine.api.model.mentors.MentorAssignBody;
import com.dev.zine.api.model.task.TaskCreateBody;
import com.dev.zine.api.model.task.TaskInstanceCreateBody;
import com.dev.zine.api.model.task.TaskListBody;
import com.dev.zine.api.model.task.UserTaskAssignBody;
import com.dev.zine.api.model.task.UserTasksBody;
import com.dev.zine.api.model.user.AssignResponse;
import com.dev.zine.api.model.user.UserResponseBody;
import com.dev.zine.exceptions.RoleNotFound;
import com.dev.zine.exceptions.TaskInstanceNotFound;
import com.dev.zine.exceptions.TaskNotFoundException;
import com.dev.zine.exceptions.UserNotFound;
import com.dev.zine.model.Role;
import com.dev.zine.model.Task;
import com.dev.zine.model.TaskInstance;
import com.dev.zine.model.User;
import com.dev.zine.service.MentorService;
import com.dev.zine.service.TaskService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    TaskService taskService;
    @Autowired
    MentorService mentorService;

    @GetMapping()
    public ResponseEntity<?> getAllTasks() {
        List<Task> allTasks = taskService.getAllTasks();
        return ResponseEntity.ok().body(Map.of("tasks",allTasks));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<?> getTaskById(@PathVariable Long taskId) throws TaskNotFoundException{
        Task task = taskService.getTask(taskId);
        return ResponseEntity.ok().body(Map.of("task", task));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getByUser(@AuthenticationPrincipal User user) {
        try {
            if( user == null) {
                throw new UserNotFound(null);
            }
            List<UserTasksBody> tasks = taskService.getUserInstances(user);
            return ResponseEntity.ok().body(Map.of("instances", tasks));
        } catch(UserNotFound e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    

    @PostMapping()
    public ResponseEntity<?> createTask(@RequestBody TaskCreateBody task) {
        Task createdTask = taskService.createTask(task);
        return ResponseEntity.ok().body(Map.of("task", createdTask));
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteTask(@RequestBody TaskListBody body) {
        if(taskService.deleteTask(body.getTaskIds())){
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{taskId}/assigned-roles")
    public ResponseEntity<?> getAssignedRoles(@PathVariable Long taskId) {
        try {
            List<Role> roles = taskService.getAssignedRoles(taskId);
            return ResponseEntity.ok().body(Map.of("roles", roles));
        } catch(TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody TaskCreateBody task) throws TaskNotFoundException{
        Task updatedTask = taskService.updateTask(id, task);
        return ResponseEntity.ok().body(Map.of("task", updatedTask));
    }

    @PostMapping("/{taskId}/instance")
    public ResponseEntity<?> createInstance(@PathVariable Long taskId, @RequestBody TaskInstanceCreateBody body) throws TaskNotFoundException {
        try{
            TaskInstance taskInstance = taskService.createInstance(taskId, body);
            return ResponseEntity.ok().body(Map.of("taskInstance", taskInstance));
        } catch(TaskNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{taskId}/instance/{instanceId}")
    public ResponseEntity<?> updateInstance(@RequestBody TaskInstanceCreateBody body, @PathVariable Long instanceId) throws TaskInstanceNotFound{
        try{
            TaskInstance instance = taskService.updateInstance(body, instanceId);
            return ResponseEntity.ok().body(instance);
        } catch(TaskInstanceNotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }
        
    }

    @DeleteMapping("/{taskId}/instance")
    public ResponseEntity<?> deleteInstance(@RequestBody InstanceListBody body) {
        try{
            taskService.deleteInstance(body.getInstanceIds());
            return ResponseEntity.ok().build();
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        
    }

    @DeleteMapping("/{taskId}/instance/{instanceId}")
    public ResponseEntity<?> deleteInstance(@PathVariable Long instanceId) {
        try{
            taskService.deleteInstance(instanceId);
            return ResponseEntity.ok().build();
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        
    }
    
    @GetMapping("/{taskId}/instance")
    public ResponseEntity<?> getAllInstances(@PathVariable Long taskId) throws TaskNotFoundException{
        try{
            List<TaskInstance> instances = taskService.getAllTaskInstances(taskId);
            return ResponseEntity.ok().body(Map.of("instances", instances));
        } catch(TaskNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{taskId}/instance/{instanceId}/assigned")
    public ResponseEntity<?> getAssigned(@PathVariable Long instanceId) throws TaskInstanceNotFound {
        try{
            List<UserResponseBody> userList = taskService.getAssigned(instanceId);
            return ResponseEntity.ok().body(Map.of("users", userList));
        } catch(TaskInstanceNotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/{taskId}/instance/{instanceId}/assign")
    public ResponseEntity<?> assignToTask(@PathVariable Long instanceId, @RequestBody UserTaskAssignBody body) throws TaskInstanceNotFound {
        try{
            AssignResponse res = taskService.assignUser(instanceId, body);
            return ResponseEntity.ok().body(res);
        } catch(TaskInstanceNotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }
    
    @PostMapping("/{taskId}/mentor")
    public ResponseEntity<?> assignMentors(@PathVariable Long taskId, @RequestBody MentorAssignBody mentorAssignBody) throws TaskNotFoundException{
        try{
            AssignResponse res = mentorService.assignMentors(taskId, mentorAssignBody);
            return ResponseEntity.ok().body(res);
        } catch(TaskNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/{taskId}/role/{roleId}")
    public ResponseEntity<?> mapTaskToRole(@PathVariable Long taskId, @PathVariable Long roleId) throws TaskNotFoundException, RoleNotFound{
        try {
            taskService.mapTaskToRole(taskId, roleId);
            return ResponseEntity.ok().build();
        } catch (TaskNotFoundException | RoleNotFound e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @Transactional
    @DeleteMapping("/{taskId}/role/{roleId}")
    public ResponseEntity<?> deleteTaskToRoleMap(@PathVariable("taskId") Long taskId, @PathVariable("roleId") Long roleId) throws TaskNotFoundException, RoleNotFound{
        try {
            if(taskService.deleteTaskToRoleMap(taskId, roleId)) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().body(Map.of("message", "Task and role do not exist"));
        } catch (TaskNotFoundException | RoleNotFound e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }    
}
