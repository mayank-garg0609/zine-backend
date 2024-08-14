package com.dev.zine.api.controllers.task;

import com.dev.zine.api.model.task.MentorAssignBody;
import com.dev.zine.api.model.task.TaskCreateBody;
import com.dev.zine.api.model.task.TaskInstanceCreateBody;
import com.dev.zine.api.model.task.UserTaskAssignBody;
import com.dev.zine.exceptions.TaskInstanceNotFound;
import com.dev.zine.exceptions.TaskNotFoundException;
import com.dev.zine.model.Task;
import com.dev.zine.model.TaskInstance;
import com.dev.zine.model.User;
import com.dev.zine.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    TaskService taskService;

    @GetMapping("/get-all")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> allTasks = taskService.getAllTasks();
        return ResponseEntity.ok().body(allTasks);
    }

    @GetMapping("/get")
    public Task getTaskById(@RequestParam Long taskId) throws TaskNotFoundException{
        return taskService.getTask(taskId);
    }

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody TaskCreateBody task) {
        Task createdTask = taskService.createTask(task);
        return ResponseEntity.ok().body(createdTask);
    }

    @PostMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteTask(@RequestBody List<Long> id) {
        if(taskService.deleteTask(id)){
            return ResponseEntity.ok().body(Map.of("status","success","message","Task "+id+" deleted successfully"));
        }
        return ResponseEntity.ok().body(Map.of("status","fail","message","Task "+id+" not found"));
    }

    @PostMapping("/update")
    public Task updateTask(@RequestParam Long id, @RequestBody TaskCreateBody task) throws TaskNotFoundException{
        return taskService.updateTask(id, task);
    }

    @PostMapping("/instance/create")
    public ResponseEntity<?> createInstance(@RequestBody TaskInstanceCreateBody body) throws TaskNotFoundException {
        try{
            TaskInstance taskInstance = taskService.createInstance(body);
            return ResponseEntity.ok().body(taskInstance);
        } catch(TaskNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }
    }

    @PostMapping("/instance/update")
    public ResponseEntity<?> updateInstance(@RequestBody TaskInstanceCreateBody body, @RequestParam Long instanceId) throws TaskInstanceNotFound{
        try{
            TaskInstance instance = taskService.updateInstance(body, instanceId);
            return ResponseEntity.ok().body(instance);
        } catch(TaskInstanceNotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        
    }

    @PostMapping("/instance/delete")
    public ResponseEntity<?> deleteInstance(@RequestBody List<Long> ids) {
        try{
            taskService.deleteInstance(ids);
            return ResponseEntity.ok().build();
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        
    }
    
    @GetMapping("/instance")
    public List<TaskInstance> getAllInstances() {
        return taskService.getAllInstances();
    }

    @GetMapping("/get-assigned")
    public List<User> getAssigned(@RequestParam Long id) {
        try{
            return taskService.getAssigned(id);
        } catch(Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException("couldn't get assigned");
        }
    }

    @PostMapping("/assign-task")
    public ResponseEntity<Map<String, String>> assignToTask(@RequestBody UserTaskAssignBody body) throws TaskInstanceNotFound {
        try{
            Map<String, String> res = taskService.assignUser(body);
            return ResponseEntity.ok().body(res);
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/assign-mentors")
    public void assignMentors(@RequestBody MentorAssignBody mentorAssignBody) {
        
    }
    
}
