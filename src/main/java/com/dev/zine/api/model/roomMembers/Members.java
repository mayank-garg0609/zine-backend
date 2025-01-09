package com.dev.zine.api.model.roomMembers;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Members {
    @NotNull
    private String userEmail;

    @NotNull
    private String role;
}
