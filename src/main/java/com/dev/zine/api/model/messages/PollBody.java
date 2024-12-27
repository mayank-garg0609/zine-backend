package com.dev.zine.api.model.messages;

import java.util.List;

import lombok.Data;

@Data
public class PollBody {
    private String title;
    private String description;
    private List<String> pollOptions;
}
