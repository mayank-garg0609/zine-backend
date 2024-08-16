package com.dev.zine.api.model.role;

import java.util.List;

import lombok.Data;

@Data
public class RoleAssignBody {
    private Long roleId;
    private List<String> userEmails;
}
