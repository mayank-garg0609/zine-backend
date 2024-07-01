package com.dev.zine.api.model.blogs;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BlogBody {
    @NotNull
    private String blog_name;

    private String blog_description;
    private String dp_url;

    @NotNull
    private Integer blogCategory;
}
