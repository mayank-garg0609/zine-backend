package com.dev.zine.basanti;

import com.dev.zine.api.model.event.EventBody;
import com.dev.zine.basanti.model.*;
import com.dev.zine.dao.*;
import com.dev.zine.exceptions.EmailNotFoundException;
import com.dev.zine.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BasantiService {
    @Autowired
    private EventDAO eventDAO;
    @Autowired
    private TaskDAO taskDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RoomMembersDAO roomMembersDAO;
    @Autowired
    private MessagesDAO messagesDAO;
    @Autowired
    private TaskMentorDAO taskMentorDAO;
    @Autowired
    private TaskInstanceDAO taskInstanceDAO;
    @Autowired
    private TaskInstanceCommentsDAO taskInstanceCommentsDAO;
    @Autowired
    private InstanceCheckpointsDAO instanceCheckpointsDAO;
    @Autowired
    private InstanceLinksDAO instanceLinksDAO;
    @Autowired
    private UserToRoleDAO userToRoleDAO;

    public List<UserBasanti> getAllUsers(){
        List<User> users = userDAO.findAll();
        List<UserBasanti> usersBasanti = new ArrayList<>();
        for(User user : users){
            UserBasanti body = new UserBasanti();
            body.setId(user.getId());
            body.setName(user.getName());
            body.setEmail(user.getEmail());
            body.setType(user.getType());
            body.setRegistered(user.isRegistered());
            body.setEmailVerified(user.isEmailVerified());
            List<UserToRole> userToRoles = userToRoleDAO.findByUser(user);
            List<String> roles = new ArrayList<>();
            for(UserToRole userToRole : userToRoles){
                Role role = userToRole.getRole();
                String roleName = role.getRoleName();
                roles.add(roleName);
            }
            body.setRoleName(roles);
            usersBasanti.add(body);
        }
        return usersBasanti;
    }

    public List<EventBody> getAllEvents(){
        List<Event> events = eventDAO.findAll();
        List<EventBody> eventsBasanti = new ArrayList<>();
        for (Event event : events) {
            EventBody body = new EventBody();
            body.setType(event.getType());
            body.setDescription(event.getDescription());
            body.setVenue(event.getVenue());
            body.setName(event.getName());
            body.setStartDateTime(event.getStartDateTime());
            body.setEndDateTime(event.getEndDateTime());

            eventsBasanti.add(body);
        }
        return eventsBasanti;
    }

    public List<ProjectBasanti> getAllProjects(){
        List<Task> tasks = taskDAO.findAll();
        List<ProjectBasanti> projects = new ArrayList<>();
        for (Task task : tasks) {
            ProjectBasanti project = new ProjectBasanti();
            project.setTitle(task.getTitle());
            project.setDescription(task.getDescription());
            projects.add(project);
        }
        return projects;
    }

    public List<ChatBasanti> getUserChats(String userEmail) throws EmailNotFoundException {
        User user = userDAO.findByEmailIgnoreCase(userEmail).orElseThrow(() -> new EmailNotFoundException());
        List<RoomMembers> roomMembers = roomMembersDAO.findByUser(user);

        List<ChatBasanti> chats = new ArrayList<>();

        for (RoomMembers roomMember : roomMembers) {
            Rooms room = roomMember.getRoom();
            Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "timestamp"));
            List<Message> latestMessages = messagesDAO.findByRoomIdOrderByTimestampDesc(room, pageable);
            for(Message message : latestMessages) {
                ChatBasanti chat = new ChatBasanti();
                chat.setContent(message.getContent());
                chat.setType(message.getType());
                chat.setContentUrl(message.getContentUrl());
                chats.add(chat);
            }

        }
        return chats;
    }

    public List<TaskBasanti> getUserProjects(String userEmail) throws EmailNotFoundException {
        User user = userDAO.findByEmailIgnoreCase(userEmail).orElseThrow(() -> new EmailNotFoundException());
        List<Task> tasks = taskDAO.findAll();
        List<TaskBasanti> projects = new ArrayList<>();
        for(Task task : tasks) {
//            if(task.isVisible()) {
                TaskBasanti project = new TaskBasanti();
                project.setCreatedDate(task.getCreatedDate());
                project.setTitle(task.getTitle());
                project.setSubTitle(task.getSubtitle());
                project.setDescription(task.getDescription());
                project.setDueDate(task.getDueDate());
                project.setPsLink(task.getPsLink());
                project.setType(task.getType());
                project.setRecruitment(task.getRecruitment());
                List<TaskMentor> taskMentors = taskMentorDAO.findByTaskId(task);
                List<User> mentors = new ArrayList<>();
                for(TaskMentor taskMentor : taskMentors) {
                  User mentor = taskMentor.getMentor();
                  mentors.add(mentor);
                }
                project.setMentors(mentors);
                List<TaskInstance> taskInstances = task.getTaskInstances();
                List<InstanceBasanti> instances = new ArrayList<>();
                for(TaskInstance taskInstance : taskInstances) {
                    InstanceBasanti instance = new InstanceBasanti();
                    instance.setType(taskInstance.getType());
                    instance.setName(taskInstance.getName());
                    instance.setStatus(taskInstance.getStatus());
                    instance.setCompletionPercentage(taskInstance.getCompletionPercentage());

                    List<String> comments = new ArrayList<>();
                    List<TaskInstanceComments> taskComments = taskInstanceCommentsDAO.findByTaskInstance(taskInstance);
                    for(TaskInstanceComments taskComment : taskComments) {
                        String comment = taskComment.getMessage();
                        comments.add(comment);
                    }
                    instance.setComments(comments);

                    List<CheckpointBasanti> checkpoints = new ArrayList<>();
                    List<InstanceCheckpoints> taskCheckpoints = instanceCheckpointsDAO.findByTaskInstance(taskInstance);
                    for(InstanceCheckpoints taskCheckpoint : taskCheckpoints) {
                        CheckpointBasanti checkpoint = new CheckpointBasanti();
                        checkpoint.setContent(taskCheckpoint.getContent());
                        checkpoint.setRemark(taskCheckpoint.isRemark());
                        checkpoints.add(checkpoint);
                    }
                    instance.setCheckpoints(checkpoints);

                    List<LinkBasanti> links = new ArrayList<>();
                    List<InstanceLinks> taskLinks = instanceLinksDAO.findByTaskInstance(taskInstance);
                    for(InstanceLinks taskLink : taskLinks) {
                        LinkBasanti link = new LinkBasanti();
                        link.setLink(taskLink.getLink());
                        link.setType(taskLink.getType());
                        links.add(link);
                    }
                    instance.setLinks(links);
                    instances.add(instance);

                }
            project.setTaskInatances(instances);
            projects.add(project);
//            }
        }
        return projects;
    }
}
