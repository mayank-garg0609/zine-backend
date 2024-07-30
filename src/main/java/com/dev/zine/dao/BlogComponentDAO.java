package com.dev.zine.dao;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.BlogComponent;
import com.dev.zine.model.Blog;

public interface BlogComponentDAO extends ListCrudRepository<BlogComponent, Long> {
    List<BlogComponent> findByBlog(Blog blog);
}   
