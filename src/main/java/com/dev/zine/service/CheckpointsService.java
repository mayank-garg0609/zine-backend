package com.dev.zine.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.zine.api.model.task.CheckpointCreateBody;
import com.dev.zine.dao.InstanceCheckpointsDAO;
import com.dev.zine.dao.TaskInstanceDAO;
import com.dev.zine.exceptions.CheckpointNotFound;
import com.dev.zine.exceptions.TaskInstanceNotFound;
import com.dev.zine.model.InstanceCheckpoints;
import com.dev.zine.model.TaskInstance;
import com.dev.zine.utils.NullAwareBeanUtilsBean;

@Service
public class CheckpointsService {
    @Autowired
    private InstanceCheckpointsDAO checkpointsDAO;
    @Autowired
    private TaskInstanceDAO instanceDAO;
    @Autowired
    private FirebaseMessagingService fcm;

    public InstanceCheckpoints addCheckpoint(Long instanceId, CheckpointCreateBody body) throws TaskInstanceNotFound{
        TaskInstance instance = instanceDAO.findById(instanceId).orElseThrow(() -> new TaskInstanceNotFound(instanceId));

        InstanceCheckpoints checkpoint = new InstanceCheckpoints();
        checkpoint.setContent(body.getContent());
        checkpoint.setRemark(body.getRemark());
        checkpoint.setTaskInstance(instance);
        checkpoint.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));

        checkpointsDAO.save(checkpoint);

        String prefix = body.getRemark() ? "[REMARK]" : "[CHECKPOINT]";
        fcm.sendNotificationToTopic("room" + instance.getRoomId().getId()+"", instance.getRoomId().getName(),
                prefix + ": " + body.getContent(), null);
        
        return checkpoint;

    }

    public List<InstanceCheckpoints> getCheckpoints(Long instanceId) throws TaskInstanceNotFound {
        TaskInstance instance = instanceDAO.findById(instanceId).orElseThrow(() -> new TaskInstanceNotFound(instanceId));
        List<InstanceCheckpoints> checkpoints = checkpointsDAO.findByTaskInstance(instance);
        return checkpoints;
    }

    public InstanceCheckpoints updateCheckpoint(Long id, CheckpointCreateBody update) throws CheckpointNotFound{
        InstanceCheckpoints existing = checkpointsDAO.findById(id).orElseThrow(() -> new CheckpointNotFound(id));
        try{
            NullAwareBeanUtilsBean beanUtilsBean = new NullAwareBeanUtilsBean();
            beanUtilsBean.copyProperties(existing, update);
            checkpointsDAO.save(existing);
            return existing;
        } catch(IllegalAccessException | InvocationTargetException e){
            return existing;
        }
    }

    public boolean deleteCheckpoint(Long id) throws CheckpointNotFound{
        try{
            checkpointsDAO.deleteById(id);
            return true;
        } catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

}
