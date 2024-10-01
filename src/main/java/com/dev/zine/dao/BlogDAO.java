package com.dev.zine.dao;
import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.Blog;

public interface BlogDAO extends ListCrudRepository<Blog, Long>{
    // List<Blog> findByParentBlog(Long id);
    List<Blog> findByParentBlog(Blog parentBlog);
    List<Blog> findByParentBlogIsNull();
}
