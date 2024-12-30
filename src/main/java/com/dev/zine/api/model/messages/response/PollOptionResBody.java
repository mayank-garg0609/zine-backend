package com.dev.zine.api.model.messages.response;

import lombok.Data;

@Data
public class PollOptionResBody {
    private Long id;
    private String value;
    private int numVotes;
}
