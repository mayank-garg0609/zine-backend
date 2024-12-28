package com.dev.zine.basanti.model;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ChatBasanti {

    private String type;

    private String content;

    private String contentUrl;
}
