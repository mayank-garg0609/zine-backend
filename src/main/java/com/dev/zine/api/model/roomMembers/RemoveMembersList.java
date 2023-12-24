package com.dev.zine.api.model.roomMembers;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RemoveMembersList {

    @NotNull
    private Long room;

    @NotNull
    @NotEmpty
    private List<String> members;
}
