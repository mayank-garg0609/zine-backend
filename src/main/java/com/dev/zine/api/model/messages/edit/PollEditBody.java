package com.dev.zine.api.model.messages.edit;

import java.util.List;

import lombok.Data;

@Data
public class PollEditBody {
    private String title;
    private String description;
    private List<PollOptionEditBody> options;
}
