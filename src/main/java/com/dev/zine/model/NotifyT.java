package com.dev.zine.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotifyT {

    @NotNull
    private String title;

    @NotNull
    private String body;

    @NotNull
    private String imageUrl;
}
