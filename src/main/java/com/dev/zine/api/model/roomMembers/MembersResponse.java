package com.dev.zine.api.model.roomMembers;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MembersResponse {

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    private String role;

    private String dpUrl;

}
