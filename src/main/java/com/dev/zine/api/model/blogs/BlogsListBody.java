package com.dev.zine.api.model.blogs;

import java.util.List;

import lombok.Data;

@Data
public class BlogsListBody {
    private List<Long> blogIds;
}
