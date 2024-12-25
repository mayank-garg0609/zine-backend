package com.dev.zine.api.model.task;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class LinkResBody {
    private Long id;
    private String type;
    private String link;
    private Timestamp timestamp;
    private String sentFrom;
    private Long sentFromId;
}
