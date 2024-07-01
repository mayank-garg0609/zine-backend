package com.dev.zine.dao;
import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.Blog;

public interface BlogDAO extends ListCrudRepository<Blog, Long>{
    Blog findByBlogID(Long blogID);
    // List<Blog> findByBlogCategory(BlogCategory blogCategory);
}
