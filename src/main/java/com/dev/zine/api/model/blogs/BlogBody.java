package com.dev.zine.api.model.blogs;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BlogBody {
    @NotNull
    private String blogName;
    private String blogDescription;
    private String dpURL;
    @NotNull
    private Long parentBlog;
    private String content; 
}
