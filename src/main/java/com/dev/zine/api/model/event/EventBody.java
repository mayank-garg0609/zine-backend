package com.dev.zine.api.model.event;

import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;

@Data
public class EventBody {
    @NotNull
    private String description;

    @NotNull
    private String type;

    @NotNull
    private String name;
    
    @NotNull
    private String venue;

    @NotNull
    private Long recruitment;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDateTime;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDateTime;
}
