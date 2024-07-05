package com.dev.zine.service.ServiceTask;

import com.dev.zine.api.model.task.Task;

import java.util.List;

public interface TaskService {
    String createTask(Task task);
    List<Task> readTasks();
    boolean deleteTask(Long id);
    String updateTask(long id , Task task);
    Task readTask(Long id);
}
