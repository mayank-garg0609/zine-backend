package com.dev.zine.api.model.messages;

import java.sql.Timestamp;

import com.dev.zine.model.Rooms;
import com.google.firebase.database.annotations.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class MessageBody {

    @NotNull
    private String type;

    private String content;

    private String contentUrl;
    private Timestamp timestamp;
    @NotNull
    private long sentFrom;
    @NotNull
    private long roomId;
    private long replyTo;
}
