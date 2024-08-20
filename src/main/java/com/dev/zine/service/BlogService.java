package com.dev.zine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.cloud.Timestamp;
import java.time.Instant;
import java.util.List;

import com.dev.zine.dao.BlogComponentDAO;
import com.dev.zine.dao.BlogDAO;
import com.dev.zine.model.BlogComponent;
import com.dev.zine.model.Blog;

import com.dev.zine.api.model.blogs.BlogBody;
import com.dev.zine.api.model.blogs.BlogComponentBody;
import com.dev.zine.exceptions.BlogNotFound;

@Service
public class BlogService {
    @Autowired
    private BlogDAO blogDAO;
    @Autowired
    private BlogComponentDAO blogComponentDAO;

    public Blog createBlog(BlogBody blogBody) throws BlogNotFound{
        Blog newBlog = new Blog();
        Blog parentBlog = null;
        if(blogBody.getParentBlog() != null){
            parentBlog = blogDAO.findById(blogBody.getParentBlog()).orElseThrow(() -> new BlogNotFound(blogBody.getParentBlog()));
        }
        newBlog.setBlogName(blogBody.getTitle());
        newBlog.setBlogDescription(blogBody.getDescription());
        newBlog.setCreatedAt(Timestamp.ofTimeSecondsAndNanos(Instant.now().getEpochSecond(), Instant.now().getNano()));
        newBlog.setDpURL(blogBody.getDp());
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
    // public void createBlogComponent(BlogComponentBody blogComponentBody) throws Exception{
    //     BlogComponent newComponent = new BlogComponent();
    //     Blog parentBlog = blogDAO.findById(blogComponentBody.getBlog_id()).orElseThrow(() -> new BlogNotFound(blogComponentBody.getBlog_id()));
    //     newComponent.setComponentType(blogComponentBody.getComponent_type());
    //     newComponent.setContent(blogComponentBody.getContent());
    //     newComponent.setRelativeOrder(blogComponentBody.getOrder());
    //     newComponent.setBlog(parentBlog);
    //     blogComponentDAO.save(newComponent);
    //     // try {
    //     // } catch(Exception e){
    //     //     throw new Exception("Could not create component");
    //     // }
    // }
    // public List<BlogComponent> getBlogComponents(Long BlogID) throws Exception{
    //     Blog parentBlog = blogDAO.findByBlogID(BlogID);
    //     return blogComponentDAO.findByBlog(parentBlog);
    //     // try {
    //     // } catch(Exception e){
    //     //     throw new Exception("Could not create component");
    //     // }
    // }
    
}
