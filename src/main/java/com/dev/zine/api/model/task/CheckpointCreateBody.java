package com.dev.zine.api.model.task;

import lombok.Data;

@Data
public class CheckpointCreateBody {
    private Boolean remark;
    private String content;
    private Long sentFromId;
}
