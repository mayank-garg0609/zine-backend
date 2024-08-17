package com.dev.zine.api.model.task;

import java.util.List;

import lombok.Data;

@Data
public class TaskListBody {
    List<Long> taskIds;
}
