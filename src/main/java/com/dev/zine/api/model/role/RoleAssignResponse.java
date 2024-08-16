package com.dev.zine.api.model.role;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleAssignResponse {
    private String status;
    private List<String> invalidEmails;
    private List<String> alreadyAssignedEmails;

    public RoleAssignResponse(String status) {
        this.status = status;
        this.invalidEmails = null;
        this.alreadyAssignedEmails = null;
    }

    public RoleAssignResponse(String status, List<String> invalidEmails, List<String> alreadyAssignedEmails) {
        this.status = status;
        this.invalidEmails = invalidEmails;
        this.alreadyAssignedEmails = alreadyAssignedEmails;
    }
}

