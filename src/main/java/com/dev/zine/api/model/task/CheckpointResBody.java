package com.dev.zine.api.model.task;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class CheckpointResBody {
    private Long id;
    private boolean remark;
    private String content;
    private Timestamp timestamp;
    private String sentFrom;
    private Long sentFromId;
}
