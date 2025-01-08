package com.dev.zine.api.model.form.creation;

import java.util.List;

import lombok.Data;

@Data
public class FormCreateBody {
    private String name;
    private String description;
    private Long eventId;
    private boolean active;
    private List<QuestionCreateBody> questions;
}
