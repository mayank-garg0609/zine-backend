package com.dev.zine.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.zine.api.model.mentors.MentorUpdateBody;
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

    public Map<String, String> assignMentors(Task task, List<String> mentorIds){
        String message = "";
        List<TaskMentor> mentorsList = new ArrayList<>();
        for(String mentorId: mentorIds){
            User user = userDAO.findByEmailIgnoreCase(mentorId).orElse(null);
            if(user != null && !taskMentorDAO.existsByMentorAndTaskId(user, task)){
                TaskMentor newMentor = new TaskMentor();
                newMentor.setMentor(user);
                newMentor.setTaskId(task);
                mentorsList.add(newMentor);
            } else{
                message += mentorId.toString() + " ";
            }
        }
        
        if(message == ""){
            taskMentorDAO.saveAll(mentorsList);
            return Map.of("message","All mentors added successfully", "status","success");
        } else{
            return Map.of("message",message, "status","fail");
        }
    }

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
    

    public void addMentors(MentorUpdateBody body) throws TaskNotFoundException{
        try{
            Task task = taskDAO.findById(body.getTaskId()).orElse(null);
            if(task != null) {
                assignMentors(task, body.getMentorIds());
                List<TaskInstance> instances = taskInstanceDAO.findByTaskId(task);
                List<User> users = userDAO.findByEmailIn(body.getMentorIds());
                for(TaskInstance instance: instances){
                    Rooms room = instance.getRoomId();
                    for(User user: users){
                        if(!roomMembersDAO.existsByUserAndRoom(user, room)){
                            RoomMembers member = new RoomMembers();
                            member.setRole("mentor");
                            member.setRoom(room);
                            member.setUser(user);
                            roomMembersDAO.save(member);
                        }
                    }
                }
            } else{
                throw new TaskNotFoundException();
            }
        } catch(TaskNotFoundException e){
            throw e;
        }
    }
}
