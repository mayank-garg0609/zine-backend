package com.dev.zine.api.model.event;

import java.util.List;

import lombok.Data;

@Data
public class EventsList {
    private List<Long> eventIds;
}
