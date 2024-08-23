package com.dev.zine.api.model.comments;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class CommentResponse {
    private Long commentId;
    private Long taskInstance;
    private Long taskId;
    private String message;
    private Long senderId;
    private String senderEmail;
    private String senderName;
    private Timestamp timestamp;
}
