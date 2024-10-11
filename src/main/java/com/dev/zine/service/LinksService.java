package com.dev.zine.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.zine.api.model.task.LinkCreateBody;
import com.dev.zine.dao.InstanceLinksDAO;
import com.dev.zine.dao.TaskInstanceDAO;
import com.dev.zine.exceptions.LinkNotFound;
import com.dev.zine.exceptions.TaskInstanceNotFound;
import com.dev.zine.model.InstanceLinks;
import com.dev.zine.model.TaskInstance;
import com.dev.zine.utils.NullAwareBeanUtilsBean;

@Service
public class LinksService {
    @Autowired
    private InstanceLinksDAO linksDAO;
    @Autowired
    private TaskInstanceDAO instanceDAO;
    @Autowired
    private FirebaseMessagingService fcm;

    public InstanceLinks addLink(Long instanceId, LinkCreateBody body) throws TaskInstanceNotFound{
        TaskInstance instance = instanceDAO.findById(instanceId).orElseThrow(() -> new TaskInstanceNotFound(instanceId));

        InstanceLinks link = new InstanceLinks();
        link.setLink(body.getLink());
        link.setType(body.getType());
        link.setTaskInstance(instance);
        link.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));

        linksDAO.save(link);

        String prefix = "[LINK]";
        fcm.sendNotificationToTopic("room" + instance.getRoomId().getId()+"", instance.getRoomId().getName(),
                prefix + ": " + body.getLink(), null);
        
        return link;

    }

    public List<InstanceLinks> getLinks(Long instanceId) throws TaskInstanceNotFound {
        TaskInstance instance = instanceDAO.findById(instanceId).orElseThrow(() -> new TaskInstanceNotFound(instanceId));
        List<InstanceLinks> links = linksDAO.findByTaskInstance(instance);
        return links;
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
