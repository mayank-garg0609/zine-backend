package com.dev.zine.api.model.recruitment;

import lombok.Data;

@Data
public class RecruitmentCreateBody {
    private String title;
    private Long stage;
    private String description;
}
