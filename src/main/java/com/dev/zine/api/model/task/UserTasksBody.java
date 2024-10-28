package com.dev.zine.api.model.task;

import com.dev.zine.model.Task;

import lombok.Data;

@Data
public class UserTasksBody {
    private Long id;
    private String type; 
    private String name;
    private String status;
    private Integer completionPercentage;
    private Task task;
    private Long roomId;
    private String roomName;
    private Long NumberOfCheckpoint;
}
