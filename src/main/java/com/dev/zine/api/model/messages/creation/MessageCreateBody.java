package com.dev.zine.api.model.messages.creation;

import com.dev.zine.api.model.messages.FileBody;
import com.dev.zine.api.model.messages.TextMsgBody;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class MessageCreateBody {
    @NotNull
    private String type;
    @NotNull
    private Long sentFrom;
    @NotNull
    private Long roomId;
    private Long replyTo;
    private FileBody file;
    private PollCreateBody poll;
    private TextMsgBody textMessage;
}
