package com.dev.zine.api.model.roomMembers;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Members {
    @NotNull
    private String userEmail;

    @NotNull
    private String role;
}
