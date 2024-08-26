package com.dev.zine.api.model.recruitment;

import lombok.Data;

import java.util.List;

@Data
public class RecruitmentListBody {
    private List<Long> recruitments;
}
