package com.dev.zine.api.model.blogs;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BlogComponentBody {
    @NotNull
    private String component_type;

    private String content;
    private Integer order;

    @NotNull
    private Long blog_id;
}
