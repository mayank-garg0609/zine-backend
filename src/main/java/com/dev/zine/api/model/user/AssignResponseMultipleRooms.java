package com.dev.zine.api.model.user;

import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AssignResponseMultipleRooms {
    private String status;
    private Set<String> invalidEmails;
    private Map<Long, List<String>> alreadyAssignedEmails;

    public AssignResponseMultipleRooms(String status) {
        this.status = status;
        this.invalidEmails = null;
        this.alreadyAssignedEmails = null;
    }

    public AssignResponseMultipleRooms(String status, Set<String> invalidEmails, Map<Long, List<String>> alreadyAssignedEmails) {
        this.status = status;
        this.invalidEmails = invalidEmails;
        this.alreadyAssignedEmails = alreadyAssignedEmails;
    }
}

