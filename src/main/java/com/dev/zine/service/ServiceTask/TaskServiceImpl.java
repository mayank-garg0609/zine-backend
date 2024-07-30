package com.dev.zine.service.ServiceTask;

import com.dev.zine.model.Task;
import com.dev.zine.dao.TaskDAO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
@org.springframework.stereotype.Service

public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskDAO taskDAO;

    @Override
    public String createTask(com.dev.zine.api.model.task.Task task) {
        Task taskEntity = new Task();
        BeanUtils.copyProperties(task, taskEntity);
        taskDAO.save(taskEntity);
        return "Task Saved successfully";
    }

    @Override
    public com.dev.zine.api.model.task.Task readTask(Long id) {
            Task tas = taskDAO.findById(id).get();
                com.dev.zine.api.model.task.Task task = new com.dev.zine.api.model.task.Task();
                BeanUtils.copyProperties(tas, task);
                return task;
    }

    @Override
    public List<com.dev.zine.api.model.task.Task> readTasks() {
        List<Task> tasksList = taskDAO.findAll();
        List<com.dev.zine.api.model.task.Task> tasks = new ArrayList<>();
        for (Task task : tasksList) {

            com.dev.zine.api.model.task.Task tas = new com.dev.zine.api.model.task.Task();
            tas.setId(task.getId());
            tas.setCreatedDate(task.getCreatedDate());
            tas.setTitle(task.getTitle());
            tas.setSubtitle(task.getSubtitle());
            tas.setDescription(task.getDescription());
            tas.setDueDate(task.getDueDate());
            tas.setPs_link(task.getPs_Link());
            tas.setSubmission_link(task.getSubmission_link());
            tas.setRoom_name(task.getRoom_name());
            tas.setCreate_room(task.isCreate_room());
            tas.setType(task.getType());
            tas.setRecruitment(task.getRecruitment());
            tas.setVisible(task.isVisible());
            tas.setRoomId(task.getRoomId());

            tasks.add(tas);
        }
        return tasks;
    }

    @Override
    public boolean deleteTask(Long id) {
        //Task tas = repository.findById(id).get();
        taskDAO.deleteById(id);
        return true;
    }

    @Override
    public String updateTask(long id, com.dev.zine.api.model.task.Task task) {
        Task existingTask = taskDAO.findById(id).get();
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

        taskDAO.save(existingTask);

        return "Task Updated";
    }
}
