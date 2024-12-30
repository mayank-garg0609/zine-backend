package com.dev.zine.api.model.messages.creation;

import java.util.List;

import lombok.Data;

@Data
public class PollCreateBody {
    private String title;
    private String description;
    private List<String> pollOptions;
}
