package com.dev.zine.service;

import com.dev.zine.api.model.userTask.UserTask;
import com.dev.zine.dao.UserTaskDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UserTaskService {

    @Autowired
    private UserTaskDAO userTaskDAO;

    public com.dev.zine.model.UserTask createUserTask(UserTask userTask){
        com.dev.zine.model.UserTask newUserTask = new com.dev.zine.model.UserTask();

        newUserTask.setTask_status(userTask.getTask_status());
        newUserTask.setCompletion_percentage(userTask.getCompletion_percentage());
        newUserTask.setTask_id(userTask.getTask_id());
        newUserTask.setRoom_id(userTask.getRoom_id());

        userTaskDAO.save(newUserTask);
        return userTask;
    }

    public boolean deleteUserTask(Long ids){
        userTaskDAO.deleteAllById(ids);
        return true;
    }

    public String updateUserTask(Long id,UserTask userTask){

            com.dev.zine.model.UserTask existingUserTask = userTaskDAO.findById(id).get();
            existingUserTask.setTask_status(userTask.getTask_status());
            existingUserTask.setCompletion_percentage(userTask.getCompletion_percentage());
            existingUserTask.setTask_id(userTask.getTask_id());
            existingUserTask.setRoom_id(userTask.getRoom_id());

            userTaskDAO.save(existingUserTask);

            return "User Task Updated";
    }

    public Optional<UserTask> getUserTaskById(Long id){

        return userTaskDAO.findById(id);
    }
}
