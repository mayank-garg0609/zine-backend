package com.dev.zine.api.model.messages.response;

import java.util.List;

import lombok.Data;

@Data
public class PollVoteRes {
    private Long chatItemId;
    private List<PollOptionResBody> pollOptions;
}
