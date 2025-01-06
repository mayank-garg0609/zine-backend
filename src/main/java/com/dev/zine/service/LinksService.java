package com.dev.zine.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.zine.api.model.task.LinkCreateBody;
import com.dev.zine.api.model.task.LinkResBody;
import com.dev.zine.dao.InstanceLinksDAO;
import com.dev.zine.dao.TaskInstanceDAO;
import com.dev.zine.dao.UserDAO;
import com.dev.zine.exceptions.LinkNotFound;
import com.dev.zine.exceptions.TaskInstanceNotFound;
import com.dev.zine.exceptions.UserNotFound;
import com.dev.zine.model.InstanceLinks;
import com.dev.zine.model.TaskInstance;
import com.dev.zine.model.User;
import com.dev.zine.utils.NullAwareBeanUtilsBean;

@Service
public class LinksService {
    @Autowired
    private InstanceLinksDAO linksDAO;
    @Autowired
    private TaskInstanceDAO instanceDAO;
    @Autowired
    private FirebaseMessagingService fcm;
    @Autowired
    private UserDAO userDAO;

    public LinkResBody addLink(Long instanceId, LinkCreateBody body) throws TaskInstanceNotFound, UserNotFound{
        TaskInstance instance = instanceDAO.findById(instanceId).orElseThrow(() -> new TaskInstanceNotFound(instanceId));
        User user = userDAO.findById(body.getSentFromId()).orElseThrow(() -> new UserNotFound());

        InstanceLinks link = new InstanceLinks();
        link.setLink(body.getLink());
        link.setType(body.getType());
        link.setTaskInstance(instance);
        link.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        link.setSentFrom(user);
        linksDAO.save(link);

        Map<String, String> bodyArgs = new HashMap<>();
        bodyArgs.put("roomId", instance.getRoomId().getId().toString());
        bodyArgs.put("linkId", link.getId().toString());
        bodyArgs.put("userEmail", user.getEmail());

        String prefix = "[LINK]";
        fcm.sendNotificationToTopic("room" + instance.getRoomId().getId()+"", instance.getRoomId().getName(),
                prefix + ": " + body.getLink(), null, bodyArgs);
        
        LinkResBody resBody = new LinkResBody();
        resBody.setId(link.getId());
        resBody.setLink(link.getLink());
        resBody.setSentFrom(link.getSentFrom().getName());
        resBody.setSentFromId(link.getSentFrom().getId());
        resBody.setTimestamp(link.getTimestamp());
        resBody.setType(link.getType());
        return resBody;

    }

    public List<LinkResBody> getLinks(Long instanceId) throws TaskInstanceNotFound {
        TaskInstance instance = instanceDAO.findById(instanceId).orElseThrow(() -> new TaskInstanceNotFound(instanceId));
        List<InstanceLinks> links = linksDAO.findByTaskInstance(instance);
        return links.stream().map(link -> {
            LinkResBody resBody = new LinkResBody();
            resBody.setId(link.getId());
            resBody.setLink(link.getLink());
            resBody.setSentFrom(link.getSentFrom().getName());
            resBody.setSentFromId(link.getSentFrom().getId());
            resBody.setTimestamp(link.getTimestamp());
            resBody.setType(link.getType());
            return resBody;
        }).collect(Collectors.toList());
    }

    public InstanceLinks updateLink(Long id, LinkCreateBody update) throws LinkNotFound{
        InstanceLinks existing = linksDAO.findById(id).orElseThrow(() -> new LinkNotFound(id));
        try{
            NullAwareBeanUtilsBean beanUtilsBean = new NullAwareBeanUtilsBean();
            beanUtilsBean.copyProperties(existing, update);
            linksDAO.save(existing);
            return existing;
        } catch(IllegalAccessException | InvocationTargetException e){
            return existing;
        }
    }

    public boolean deleteLink(Long id) {
        try{
            linksDAO.deleteById(id);
            return true;
        } catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
}
