package com.dev.zine.api.model.task;

import lombok.Data;

@Data
public class LinkCreateBody {
    private String type;
    private String link;
    private Long sentFromId;
}
