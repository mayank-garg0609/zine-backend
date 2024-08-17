package com.dev.zine.api.model.roomMembers;

import lombok.Data;

@Data
public class MemberRoleUpdate {
    private String memberEmail;
    private Long room;
    private String role;
}
