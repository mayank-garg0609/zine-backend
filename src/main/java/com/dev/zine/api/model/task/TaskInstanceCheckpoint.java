package com.dev.zine.api.model.task;


import com.dev.zine.model.*;
import lombok.Data;

import java.util.List;

@Data
public class TaskInstanceCheckpoint {

    private Long id;
    private Task taskId;
    private Rooms roomId;
    private String type;
    private String name;
    private String status;
    private Integer completionPercentage;
    private List<UserTaskAssigned> userTaskAssigned;
    private List<TaskInstanceComments> taskInstanceComments;
    private List<InstanceLinks> instanceLinks;
    private List<InstanceCheckpoints> instanceCheckpoints;
    private Long checkpointCount;


}
