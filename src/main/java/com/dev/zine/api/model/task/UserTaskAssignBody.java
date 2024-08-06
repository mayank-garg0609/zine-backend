package com.dev.zine.api.model.task;

import java.util.List;

import lombok.Data;

@Data
public class UserTaskAssignBody {
    private List<Long> userIds;
    private Long taskInstanceId;
}
