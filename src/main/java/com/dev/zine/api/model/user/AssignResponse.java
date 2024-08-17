package com.dev.zine.api.model.user;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AssignResponse {
    private String status;
    private List<String> invalidEmails;
    private List<String> alreadyAssignedEmails;

    public AssignResponse(String status) {
        this.status = status;
        this.invalidEmails = null;
        this.alreadyAssignedEmails = null;
    }

    public AssignResponse(String status, List<String> invalidEmails, List<String> alreadyAssignedEmails) {
        this.status = status;
        this.invalidEmails = invalidEmails;
        this.alreadyAssignedEmails = alreadyAssignedEmails;
    }
}

