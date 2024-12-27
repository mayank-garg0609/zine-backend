package com.dev.zine.api.model.messages;

import lombok.Data;

@Data
public class FileBody {
    private String url;
    private String description;
    private String name;
}
