package com.dev.zine.api.model.task;

import java.util.List;

import lombok.Data;

@Data
public class MentorAssignBody {
    private Long taskId;
    private List<String> mentorIds;
}
