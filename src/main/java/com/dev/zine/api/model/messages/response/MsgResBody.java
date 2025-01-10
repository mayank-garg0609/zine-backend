package com.dev.zine.api.model.messages.response;

import java.sql.Timestamp;

import com.dev.zine.api.model.messages.FileBody;
import com.dev.zine.api.model.messages.TextMsgBody;

import lombok.Data;

@Data
public class MsgResBody {
    private Long id;
    private String type;
    private TextMsgBody text;
    private FileBody file;
    private PollResBody poll;
    private SentFromBody sentFrom;
    private MsgReplyBody replyTo;
    private Timestamp timestamp;
    private boolean deleted;
}
