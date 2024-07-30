package com.dev.zine.api.model.taskMentors;

import com.dev.zine.api.model.task.Task;
import com.dev.zine.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TaskMentors {
    private Task task_id;
    private User mentor;
}
