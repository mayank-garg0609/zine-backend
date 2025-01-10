package com.dev.zine.api.model.form.form_response;

import com.dev.zine.api.model.messages.PollVoteBody;
import com.dev.zine.api.model.messages.TextMsgBody;

import lombok.Data;

@Data
public class FormResponseBody {
    private Long questionId;
    private TextMsgBody textResponse;
    private PollVoteBody pollResponse;
}
