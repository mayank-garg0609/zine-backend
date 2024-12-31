package com.dev.zine.api.model.messages.response;

import lombok.Data;

@Data
public class BroadcastMsgBody {
    private String update;
    private MsgResBody body;
    private PollVoteRes pollUpdate;
    private String errorMessage;
}
