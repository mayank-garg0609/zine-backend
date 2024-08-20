package com.dev.zine.api.model.blogs;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BlogBody {
    @NotNull
    private String title;
    private String description;
    private String dp;
    @NotNull
    private Long parentBlog;
}
