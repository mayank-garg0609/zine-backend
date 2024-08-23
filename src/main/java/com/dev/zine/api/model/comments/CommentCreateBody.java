package com.dev.zine.api.model.comments;

import lombok.Data;

@Data
public class CommentCreateBody {
    private String message;
    private Long senderId;
}
