package com.dev.zine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.cloud.Timestamp;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.List;

import com.dev.zine.dao.BlogDAO;
import com.dev.zine.model.Blog;
import com.dev.zine.utils.NullAwareBeanUtilsBean;
import com.dev.zine.api.model.blogs.BlogBody;
import com.dev.zine.exceptions.BlogNotFound;

@Service
public class BlogService {
    @Autowired
    private BlogDAO blogDAO;

    public Blog createBlog(BlogBody blogBody) throws BlogNotFound{
        Blog newBlog = new Blog();
        Blog parentBlog = null;
        if(blogBody.getParentBlog() != null){
            parentBlog = blogDAO.findById(blogBody.getParentBlog()).orElseThrow(() -> new BlogNotFound(blogBody.getParentBlog()));
        }
        newBlog.setBlogName(blogBody.getBlogName());
        newBlog.setBlogDescription(blogBody.getBlogDescription());
        newBlog.setCreatedAt(Timestamp.ofTimeSecondsAndNanos(Instant.now().getEpochSecond(), Instant.now().getNano()));
        newBlog.setContent(blogBody.getContent());
        newBlog.setDpURL(blogBody.getDpURL());
        newBlog.setParentBlog(parentBlog);
        blogDAO.save(newBlog);
        return newBlog;
    }

    public List<Blog> fetchChildrenBlogs(Long id) throws BlogNotFound{
        if(id==-1){
            return blogDAO.findByParentBlogIsNull();
        } else{
            Blog parentBlog = blogDAO.findById(id).orElseThrow(() -> new BlogNotFound(id));
            return blogDAO.findByParentBlog(parentBlog);
        }
    }

    public Blog updateBlog(Long blogId, BlogBody update) throws BlogNotFound{
        Blog existingBlog = blogDAO.findById(blogId).orElseThrow(() -> new BlogNotFound(blogId));
        try{
            NullAwareBeanUtilsBean beanUtilsBean = new NullAwareBeanUtilsBean();
            beanUtilsBean.copyProperties(existingBlog, update);
            blogDAO.save(existingBlog);
            return existingBlog;
        } catch(IllegalAccessException | InvocationTargetException e){
            return existingBlog;
        }
    }

    public boolean deleteBlog(List<Long> blogIds){
        try{
            blogDAO.deleteAllById(blogIds);
            return true;
        } catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
}
