package com.dev.zine.api.model.form.creation;

import com.dev.zine.api.model.messages.TextMsgBody;
import com.dev.zine.api.model.messages.creation.PollCreateBody;

import lombok.Data;

@Data
public class QuestionCreateBody {
    private String type;
    private boolean required;
    private TextMsgBody text;
    private PollCreateBody poll;
}
