package com.dev.zine.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.zine.api.model.mentors.MentorAssignBody;
import com.dev.zine.api.model.user.AssignResponse;
import com.dev.zine.dao.RoomMembersDAO;
import com.dev.zine.dao.TaskDAO;
import com.dev.zine.dao.TaskInstanceDAO;
import com.dev.zine.dao.TaskMentorDAO;
import com.dev.zine.dao.UserDAO;
import com.dev.zine.exceptions.TaskNotFoundException;
import com.dev.zine.model.RoomMembers;
import com.dev.zine.model.Rooms;
import com.dev.zine.model.Task;
import com.dev.zine.model.TaskInstance;
import com.dev.zine.model.TaskMentor;
import com.dev.zine.model.User;

@Service
public class MentorService {
    @Autowired
    private TaskMentorDAO taskMentorDAO;
    @Autowired
    private TaskDAO taskDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RoomMembersDAO roomMembersDAO;
    @Autowired
    private TaskInstanceDAO taskInstanceDAO;

    public void addMentorsToRoom(Task task, Rooms room){
        List<TaskMentor> mentors = taskMentorDAO.findByTaskId(task);
        List<RoomMembers> mentorRooms = new ArrayList<>();
        for(TaskMentor mentor: mentors){
            RoomMembers mentorRoom = new RoomMembers();
            mentorRoom.setRole("mentor");
            mentorRoom.setRoom(room);
            mentorRoom.setUser(mentor.getMentor());
            mentorRooms.add(mentorRoom);
        }
        roomMembersDAO.saveAll(mentorRooms);
    }
    
    public AssignResponse assignMentors(Long taskId, MentorAssignBody body) throws TaskNotFoundException{
        Task task = taskDAO.findById(taskId).orElseThrow(()-> new TaskNotFoundException(taskId));
        List<TaskInstance> instances = taskInstanceDAO.findByTaskId(task);

        List<String> invalidEmails = new ArrayList<>();
        List<String> alreadyAssignedUser = new ArrayList<>();

        List<RoomMembers> roomMembers = new ArrayList<>();
        List<TaskMentor> mentors = new ArrayList<>();

        for(TaskInstance instance: instances){
            Rooms room = instance.getRoomId();
            for(String email: body.getMentorEmails()){
                User user = userDAO.findByEmailIgnoreCase(email).orElse(null);
                if(user != null && !taskMentorDAO.existsByMentorAndTaskId(user, task)){
                    RoomMembers member = new RoomMembers();
                    member.setRole("mentor");
                    member.setRoom(room);
                    member.setUser(user);
                    roomMembers.add(member);

                    TaskMentor newMentor = new TaskMentor();
                    newMentor.setMentor(user);
                    newMentor.setTaskId(task);
                    mentors.add(newMentor);
                } else if(user != null){
                    alreadyAssignedUser.add(email);
                } else {
                    invalidEmails.add(email);
                }
            }
        }

        if(invalidEmails.isEmpty() && alreadyAssignedUser.isEmpty()){
            roomMembersDAO.saveAll(roomMembers);
            taskMentorDAO.saveAll(mentors);
            return new AssignResponse("success");
        } 
        return new AssignResponse("fail", invalidEmails, alreadyAssignedUser);
    }
}
