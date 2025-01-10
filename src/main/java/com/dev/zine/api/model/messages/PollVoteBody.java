package com.dev.zine.api.model.messages;

import lombok.Data;

@Data
public class PollVoteBody {
    private Long chatId;
    private Long optionId;
    private Long voterId;
}
