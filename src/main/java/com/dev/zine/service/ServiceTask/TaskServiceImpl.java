package com.dev.zine.service.ServiceTask;

import com.dev.zine.api.model.task.Task;
import com.dev.zine.model.TaskEntity;
import com.dev.zine.dao.TaskRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
@org.springframework.stereotype.Service

public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    //List<Task> employees = new ArrayList<>();

    @Override
    public String createTask(Task task) {
        TaskEntity taskEntity = new TaskEntity();
        BeanUtils.copyProperties(task, taskEntity);
        taskRepository.save(taskEntity);
        return "Task Saved successfully";
    }

    @Override
    public Task readTask(Long id) {
        TaskEntity tas = taskRepository.findById(id).get();
        Task task = new Task();
        BeanUtils.copyProperties(tas, task);
        return task;
    }

    @Override
    public List<Task> readTasks() {
        List<TaskEntity> tasksList = taskRepository.findAll();
        List<Task> tasks = new ArrayList<>();
        for (TaskEntity taskEntity : tasksList) {

            Task tas = new Task();
            tas.setId(taskEntity.getId());
            tas.setCreatedDate(taskEntity.getCreatedDate());
            tas.setTitle(taskEntity.getTitle());
            tas.setSubtitle(taskEntity.getSubtitle());
            tas.setDescription(taskEntity.getDescription());
            tas.setDueDate(taskEntity.getDueDate());
            tas.setPs_link(taskEntity.getPs_Link());
            tas.setSubmission_link(taskEntity.getSubmission_link());
            tas.setRoom_name(taskEntity.getRoom_name());
            tas.setCreate_room(taskEntity.isCreate_room());
            tas.setType(taskEntity.getType());
            tas.setRecruitment(taskEntity.getRecruitment());
            tas.setVisible(taskEntity.isVisible());

            tasks.add(tas);
        }
        return tasks;
    }

    @Override
    public boolean deleteTask(Long id) {
        //TaskEntity tas = repository.findById(id).get();
        taskRepository.deleteById(id);
        return true;
    }

    @Override
    public String updateTask(long id, Task task) {
        TaskEntity existingTask = taskRepository.findById(id).get();
        existingTask.setTitle(task.getTitle());
        existingTask.setSubtitle(task.getSubtitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setPs_Link(task.getPs_link());
        existingTask.setSubmission_link(task.getSubmission_link());
        existingTask.setRoom_name(task.getRoom_name());
        existingTask.setCreate_room(task.isCreate_room());
        existingTask.setType(task.getType());
        existingTask.setRecruitment(task.getRecruitment());
        existingTask.setVisible(task.isVisible());

        taskRepository.save(existingTask);

        return "Task Updated";
    }
}
