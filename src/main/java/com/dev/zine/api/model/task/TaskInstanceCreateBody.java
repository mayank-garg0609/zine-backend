package com.dev.zine.api.model.task;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskInstanceCreateBody {
    private String type;
    @NotNull
    private Long taskId;
    private String roomName;
    private String dpUrl;
    private String description;
}
