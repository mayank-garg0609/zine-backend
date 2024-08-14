package com.dev.zine.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.zine.dao.RoomMembersDAO;
import com.dev.zine.dao.TaskDAO;
import com.dev.zine.dao.TaskInstanceDAO;
import com.dev.zine.dao.UserDAO;
import com.dev.zine.dao.UserTaskAssignedDAO;
import com.dev.zine.exceptions.TaskInstanceNotFound;
import com.dev.zine.exceptions.TaskNotFoundException;
import com.dev.zine.model.RoomMembers;
import com.dev.zine.model.Rooms;
import com.dev.zine.model.Task;
import com.dev.zine.model.TaskInstance;
import com.dev.zine.model.User;
import com.dev.zine.utils.NullAwareBeanUtilsBean;
import com.dev.zine.api.model.room.RoomBody;
import com.dev.zine.api.model.task.TaskCreateBody;
import com.dev.zine.api.model.task.TaskInstanceCreateBody;
import com.dev.zine.api.model.task.UserTaskAssignBody;
import com.dev.zine.model.UserTaskAssigned;

@Service
public class TaskService {
    @Autowired
    private TaskDAO taskDAO;
    @Autowired
    private RoomService roomService;
    @Autowired
    private TaskInstanceDAO taskInstanceDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RoomMembersDAO roomMembersDAO;
    @Autowired
    private UserTaskAssignedDAO userTaskAssignedDAO;
    @Autowired
    private MentorService mentorService;

    public Task createTask(TaskCreateBody task){
        Task newTask = new Task();
        newTask.setCreatedDate(task.getCreatedDate());
        newTask.setDescription(task.getDescription());
        newTask.setDueDate(task.getDueDate());
        newTask.setPsLink(task.getPsLink());
        newTask.setRecruitment(task.getRecruitment());
        newTask.setSubmissionLink(task.getSubmissionLink());
        newTask.setSubtitle(task.getSubtitle());
        newTask.setTitle(task.getTitle());
        newTask.setType(task.getType());
        newTask.setVisible(task.isVisible());
        taskDAO.save(newTask);
        if(task.getMentorIds() != null)
            mentorService.assignMentors(newTask, task.getMentorIds());
        return newTask;
    }

    public Task getTask(Long id) throws TaskNotFoundException {
        try {
            Optional<Task> task = taskDAO.findById(id);
            if (task.isPresent()) {
                return task.get();
            } else {
                throw new TaskNotFoundException();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while retrieving task", e);
        }
    }
    

    public boolean deleteTask(List<Long> ids){
        try{
            taskDAO.deleteAllById(ids);
            return true;
        } catch(Exception e){
            System.out.println(e);
        }
        return false;
    }

    public List<Task> getAllTasks(){
        return taskDAO.findAll();
    }

    public Task updateTask(Long id, TaskCreateBody update) throws TaskNotFoundException{
        Task existingRoom = taskDAO.findById(id).orElse(null);
        if(existingRoom != null){
            try{
                NullAwareBeanUtilsBean beanUtilsBean = new NullAwareBeanUtilsBean();
                beanUtilsBean.copyProperties(existingRoom, update);
                taskDAO.save(existingRoom);
                return existingRoom;
            } catch(IllegalAccessException | InvocationTargetException e){
                return existingRoom;
            }
        } else{
            throw new TaskNotFoundException();
        }
    }
    
    public TaskInstance createInstance(TaskInstanceCreateBody body) throws TaskNotFoundException {
        try {
            Optional<Task> opTask = taskDAO.findById(body.getTaskId());
            if(opTask.isPresent()){
                Task task = opTask.get();
                RoomBody newRoom = new RoomBody();
                newRoom.setDescription(body.getDescription());
                newRoom.setName(body.getRoomName());
                newRoom.setType("project");
                newRoom.setDpUrl(body.getDpUrl());
                Rooms room = roomService.createRoom(newRoom);
                TaskInstance newInstance = new TaskInstance();
                newInstance.setRoomId(room);
                newInstance.setTaskId(task);
                newInstance.setType(body.getType());
                taskInstanceDAO.save(newInstance);
                mentorService.addMentorsToRoom(task, room);
                return newInstance;
            } else{
                throw new TaskNotFoundException();
            }
        } catch(TaskNotFoundException e){
            throw e;
        }
    }

    public TaskInstance updateInstance(TaskInstanceCreateBody update, Long taskInstance) throws TaskInstanceNotFound {
        try{
            TaskInstance existingInstance = taskInstanceDAO.findById(taskInstance).orElse(null);
            if(existingInstance != null){
                try{
                    NullAwareBeanUtilsBean beanUtilsBean = new NullAwareBeanUtilsBean();
                    beanUtilsBean.copyProperties(existingInstance, update);
                    taskInstanceDAO.save(existingInstance);
                    return existingInstance;
                } catch(IllegalAccessException | InvocationTargetException e){
                    return existingInstance;
                }
            } else{
                throw new TaskInstanceNotFound("Task instance "+taskInstance.toString()+ " not found.");
            }
        } catch(TaskInstanceNotFound e){
            throw e;
        }
    }

    public boolean deleteInstance(List<Long> taskInstances){
        try{
            taskInstanceDAO.deleteAllById(taskInstances);
            return true;
        } catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Map<String, String> assignUser(UserTaskAssignBody assign) throws TaskInstanceNotFound{
        try{
            Optional<TaskInstance> opTaskInstance = taskInstanceDAO.findById(assign.getTaskInstanceId());
            if(opTaskInstance.isPresent()){
                TaskInstance taskInstance = opTaskInstance.get();
                Rooms room = taskInstance.getRoomId();
                List<RoomMembers> roomMembers = new ArrayList<>();
                List<UserTaskAssigned> assignedUsers = new ArrayList<>();
                String message = "";
                for(Long userId: assign.getUserIds()){
                    User user = userDAO.findById(userId).orElse(null);
                    if(user != null){
                        RoomMembers roomMember = new RoomMembers();
                        roomMember.setRoom(room);
                        roomMember.setUser(user);
                        roomMember.setRole("user");
                        roomMembers.add(roomMember);

                        UserTaskAssigned assignedUser = new UserTaskAssigned();
                        assignedUser.setTaskInstanceId(taskInstance);
                        assignedUser.setUserId(user);
                        assignedUsers.add(assignedUser);
                    } else{
                        message += userId.toString() + " ";
                    }
                }
                roomMembersDAO.saveAll(roomMembers);
                userTaskAssignedDAO.saveAll(assignedUsers);
                if (message.equals("")) {
                    return Map.of("message","All users added to room " + room.getName(),"status","success");
                } else {
                    return Map.of("message",message + " :Users do not exist","status","fail");
                }
            }
             else{
                throw new TaskInstanceNotFound("Task instance "+assign.getTaskInstanceId().toString()+ " not found.");
            }
        } catch(Exception e){
            throw new RuntimeException("Error assigning task");
        }
        
    }

    public List<TaskInstance> getAllInstances(){
        return taskInstanceDAO.findAll();
    }

    public List<User> getAssigned(Long taskInstanceId){
        TaskInstance instance = taskInstanceDAO.findById(taskInstanceId).orElse(null);
        List<User> users = new ArrayList<>();
        if(instance != null){
            List<UserTaskAssigned> assigned = userTaskAssignedDAO.findByTaskInstanceId(instance);
            for(UserTaskAssigned assignee: assigned ){
                User user = userDAO.findById(assignee.getUserId().getId()).orElse(null);
                if(user != null){
                    users.add(user);
                }
            }
        }
        System.out.println("reached outside");
        return users;
    }

}
