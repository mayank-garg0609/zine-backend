package com.dev.zine.api.model.messages;

import java.sql.Timestamp;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class MessageBody {
    @NotNull
    private String type;
    @NotNull
    private Long sentFrom;
    @NotNull
    private Long roomId;
    private Long replyTo;
    private FileBody file;
    private PollBody poll;
    private TextMsgBody textMessage;
}
