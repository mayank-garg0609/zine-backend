package com.dev.zine.api.model.mentors;

import java.util.List;

import lombok.Data;

@Data
public class MentorUpdateBody {
    private Long taskId;
    private List<String> mentorIds;
}
