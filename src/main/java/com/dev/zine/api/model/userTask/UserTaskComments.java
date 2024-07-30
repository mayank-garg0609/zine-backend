package com.dev.zine.api.model.userTask;

import com.dev.zine.model.User;
import com.dev.zine.model.UserTask;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserTaskComments {
    private UserTask user_task_id;
    private String message;
    private User sender_id;
    private Date date_time;
}
