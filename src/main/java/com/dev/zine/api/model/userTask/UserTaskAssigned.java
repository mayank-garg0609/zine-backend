package com.dev.zine.api.model.userTask;

import com.dev.zine.api.model.task.Task;
import com.dev.zine.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserTaskAssigned {
    private Task task_id;
    private User user_id;
    private String status;
}
