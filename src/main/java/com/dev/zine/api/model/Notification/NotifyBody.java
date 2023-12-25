package com.dev.zine.api.model.Notification;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotifyBody {

    @NotNull
    private String title;

    @NotNull
    private String body;

    @NotNull
    private String imageUrl;
}
