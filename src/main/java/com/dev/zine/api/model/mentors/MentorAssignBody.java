package com.dev.zine.api.model.mentors;

import java.util.List;

import lombok.Data;

@Data
public class MentorAssignBody {
    private List<String> mentorEmails;
}
