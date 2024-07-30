package com.dev.zine.api.controllers.task;

import com.dev.zine.api.model.task.Task;
import com.dev.zine.service.ServiceTask.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    //dependency Injection
    @Autowired
    TaskService taskService;

    @GetMapping("/get")
    public List<Task> getAllTasks() {
        return taskService.readTasks();
    }

    @GetMapping("/get/{id}")
    public Task getAllTaskById(@PathVariable Long id) {
        return taskService.readTask(id);
    }

    @PostMapping("/create")
    public String createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        if(taskService.deleteTask(id))
            return "Task deleted successfully";
        return "Task not found";
    }

    @PostMapping("/update/{id}")
    public String updateTask(@PathVariable Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }
}
