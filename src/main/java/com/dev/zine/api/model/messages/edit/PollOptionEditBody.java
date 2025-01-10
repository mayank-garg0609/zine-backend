package com.dev.zine.api.model.messages.edit;

import lombok.Data;

@Data
public class PollOptionEditBody {
    private String action; 
    private Long optionId;
    private String value;
}

