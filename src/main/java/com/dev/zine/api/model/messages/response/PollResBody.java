package com.dev.zine.api.model.messages.response;

import java.util.List;

import lombok.Data;

@Data
public class PollResBody {
    private Long id;
    private String title;
    private String description;
    private List<PollOptionResBody> options;
}
