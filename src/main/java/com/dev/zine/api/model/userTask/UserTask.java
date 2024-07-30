package com.dev.zine.api.model.userTask;

import com.dev.zine.api.model.room.RoomBody;
import com.dev.zine.model.Task;
import lombok.Data;

@Data
public class UserTask {
    private Long id;
    private Task task_id;
    private String task_status;
    private String completion_percentage;
    private RoomBody room_id;
}
