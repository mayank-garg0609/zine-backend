package com.dev.zine.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.zine.dao.RoleDAO;
import com.dev.zine.dao.RoomMembersDAO;
import com.dev.zine.dao.TaskDAO;
import com.dev.zine.dao.TaskInstanceDAO;
import com.dev.zine.dao.TaskToRoleDAO;
import com.dev.zine.dao.UserDAO;
import com.dev.zine.dao.UserTaskAssignedDAO;
import com.dev.zine.exceptions.RoleNotFound;
import com.dev.zine.exceptions.TaskInstanceNotFound;
import com.dev.zine.exceptions.TaskNotFoundException;
import com.dev.zine.model.Role;
import com.dev.zine.model.RoomMembers;
import com.dev.zine.model.Rooms;
import com.dev.zine.model.Task;
import com.dev.zine.model.TaskInstance;
import com.dev.zine.model.TaskToRole;
import com.dev.zine.model.User;
import com.dev.zine.utils.NullAwareBeanUtilsBean;
import com.dev.zine.api.model.room.RoomBody;
import com.dev.zine.api.model.task.TaskCreateBody;
import com.dev.zine.api.model.task.TaskInstanceCreateBody;
import com.dev.zine.api.model.task.UserTaskAssignBody;
import com.dev.zine.api.model.task.UserTasksBody;
import com.dev.zine.api.model.user.AssignResponse;
import com.dev.zine.api.model.user.UserResponseBody;
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
    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private TaskToRoleDAO taskToRoleDAO;
    @Autowired
    private RoleService roleService;

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
        // if(task.getMentorIds() != null)
        //     mentorService.assignMentors(newTask, task.getMentorIds());
        return newTask;
    }

    public Task getTask(Long id) throws TaskNotFoundException {
        try {
            Optional<Task> task = taskDAO.findById(id);
            if (task.isPresent()) {
                return task.get();
            } else {
                throw new TaskNotFoundException(id);
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
            return false;
        }
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
            throw new TaskNotFoundException(id);
        }
    }
    
    public TaskInstance createInstance(Long taskId, TaskInstanceCreateBody body) throws TaskNotFoundException {
        try {
            Optional<Task> opTask = taskDAO.findById(taskId);
            if(opTask.isPresent()){
                Task task = opTask.get();
                RoomBody newRoom = new RoomBody();
                newRoom.setDescription(body.getDescription());
                newRoom.setName(body.getName());
                newRoom.setType("project");
                newRoom.setDpUrl(body.getDpUrl());
                Rooms room = roomService.createRoom(newRoom);
                TaskInstance newInstance = new TaskInstance();
                newInstance.setRoomId(room);
                newInstance.setTaskId(task);
                newInstance.setName(body.getName());
                newInstance.setType(body.getType());
                newInstance.setCompletionPercentage(body.getCompletionPercentage());
                newInstance.setStatus(body.getStatus());
                taskInstanceDAO.save(newInstance);
                mentorService.addMentorsToRoom(task, room);
                return newInstance;
            } else{
                throw new TaskNotFoundException(taskId);
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
                throw new TaskInstanceNotFound(taskInstance);
            }
        } catch(TaskInstanceNotFound e){
            throw e;
        } catch(Exception e){
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

    public List<Role> getAssignedRoles(Long taskId) throws TaskNotFoundException{
        Task task = taskDAO.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
        List<TaskToRole> taskToRoles = taskToRoleDAO.findByTaskId(task);
        return taskToRoles.stream().map(taskToRole -> {
            return taskToRole.getRoleId();
        }).collect(Collectors.toList());
    }

    public boolean deleteInstance(Long taskInstance){
        try{
            taskInstanceDAO.deleteById(taskInstance);
            return true;
        } catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public AssignResponse assignUser(Long instanceId, UserTaskAssignBody assign) throws TaskInstanceNotFound{
        TaskInstance taskInstance = taskInstanceDAO.findById(instanceId).orElseThrow(()->new TaskInstanceNotFound(instanceId));
        if(assign.getUserEmails().isEmpty()) return new AssignResponse("fail");
        List<String> invalidEmails = new ArrayList<>();
        List<String> alreadyAssignedUser = new ArrayList<>();

        
        Rooms room = taskInstance.getRoomId();

        List<RoomMembers> roomMembers = new ArrayList<>();
        List<UserTaskAssigned> assignedUsers = new ArrayList<>();

        
        for(String userEmail: assign.getUserEmails()){
            User user = userDAO.findByEmailIgnoreCase(userEmail).orElse(null);
            if(user != null && !userTaskAssignedDAO.existsByTaskInstanceIdAndUserId(taskInstance, user)){
                RoomMembers roomMember = new RoomMembers();
                roomMember.setRoom(room);
                roomMember.setUser(user);
                roomMember.setRole("user");
                roomMembers.add(roomMember);

                UserTaskAssigned assignedUser = new UserTaskAssigned();
                assignedUser.setTaskInstanceId(taskInstance);
                assignedUser.setUserId(user);
                assignedUsers.add(assignedUser);
            } else if(user != null){
                alreadyAssignedUser.add(userEmail);
            } else {
                invalidEmails.add(userEmail);
            }
        }

        if(invalidEmails.isEmpty() && alreadyAssignedUser.isEmpty()){
            roomMembersDAO.saveAll(roomMembers);
            userTaskAssignedDAO.saveAll(assignedUsers);
            return new AssignResponse("success");
        } else{
            return new AssignResponse("fail", invalidEmails, alreadyAssignedUser);
        } 
    }

    public List<TaskInstance> getAllTaskInstances(Long taskId) throws TaskNotFoundException{
        Task task = taskDAO.findById(taskId).orElseThrow(()-> new TaskNotFoundException(taskId));
        return taskInstanceDAO.findByTaskId(task);
    }

    public List<UserResponseBody> getAssigned(Long taskInstanceId) throws TaskInstanceNotFound{
        TaskInstance instance = taskInstanceDAO.findById(taskInstanceId).orElseThrow(()-> new TaskInstanceNotFound(taskInstanceId));
        List<UserTaskAssigned> assigned = userTaskAssignedDAO.findByTaskInstanceId(instance);
        return assigned.stream().map(user -> {
                    UserResponseBody userBody = new UserResponseBody();
                    userBody.setEmail(user.getUserId().getEmail());
                    userBody.setName(user.getUserId().getName());
                    return userBody;
                }).collect(Collectors.toList());
    }

    public List<UserTasksBody> getUserInstances(User user) {
        List<UserTaskAssigned> assigned = userTaskAssignedDAO.findByUserId(user);
        return assigned.stream().map(taskAssigned -> {
            UserTasksBody body = new UserTasksBody();
            body.setCompletionPercentage(taskAssigned.getTaskInstanceId().getCompletionPercentage());
            body.setName(taskAssigned.getTaskInstanceId().getName());
            body.setStatus(taskAssigned.getTaskInstanceId().getStatus());
            body.setType(taskAssigned.getTaskInstanceId().getType());
            body.setId(taskAssigned.getTaskInstanceId().getTaskInstanceId());
            body.setTask(taskAssigned.getTaskInstanceId().getTaskId());
            body.setRoomId(taskAssigned.getTaskInstanceId().getRoomId().getId());
            body.setRoomName(taskAssigned.getTaskInstanceId().getRoomId().getName());
            return body;
        }).collect(Collectors.toList());
    }

    public void mapTaskToRole(Long taskId, Long roleId) throws TaskNotFoundException, RoleNotFound{
        Task task = taskDAO.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
        Role role = roleDAO.findById(roleId).orElseThrow(() -> new RoleNotFound());
        if(taskToRoleDAO.existsByTaskIdAndRoleId(task, role)) return;
        TaskToRole newMapping = new TaskToRole();
        newMapping.setRoleId(role);
        newMapping.setTaskId(task);
        taskToRoleDAO.save(newMapping);
    } 

    public boolean deleteTaskToRoleMap(Long taskId, Long roleId) throws TaskNotFoundException, RoleNotFound{
        Task task = taskDAO.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
        Role role = roleDAO.findById(roleId).orElseThrow(() -> new RoleNotFound());
        if(taskToRoleDAO.existsByTaskIdAndRoleId(task, role)) {
            taskToRoleDAO.deleteByTaskIdAndRoleId(task, role);
            return true;
        }
        return false;
    } 

    public List<Task> getTasksForUserRoles(User user) {
        List<Role> roles = roleService.getUserRoles(user);
        List<Task> tasks = new ArrayList<>();
        for(Role role: roles) {
            List<TaskToRole> taskToRoles = taskToRoleDAO.findByRoleId(role);
            for(TaskToRole taskToRole:  taskToRoles) {
                tasks.add(taskToRole.getTaskId());
            }
        }
        return tasks;
    }

    public UserTasksBody chooseTaskByUser(Long taskId, User user, TaskInstanceCreateBody body) throws TaskNotFoundException{
        TaskInstance instance = createInstance(taskId, body);
        UserTaskAssigned assigned = new UserTaskAssigned();
        assigned.setTaskInstanceId(instance);
        assigned.setUserId(user);
        userTaskAssignedDAO.save(assigned);
        Rooms room = instance.getRoomId();
        RoomMembers member = new RoomMembers();
        member.setRoom(room);
        member.setUser(user);
        member.setRole("user");
        roomMembersDAO.save(member);

        UserTasksBody resBody = new UserTasksBody();
        resBody.setCompletionPercentage(instance.getCompletionPercentage());
        resBody.setName(instance.getName());
        resBody.setStatus(instance.getStatus());
        resBody.setType(instance.getType());
        resBody.setId(instance.getTaskInstanceId());
        resBody.setTask(instance.getTaskId());
        resBody.setRoomId(instance.getRoomId().getId());
        resBody.setRoomName(instance.getRoomId().getName());
        return resBody;
    }

    public List<TaskInstance> getEveryInstance() {
        return taskInstanceDAO.findAll();
    }

}
