package com.dev.zine.service;

import org.springframework.stereotype.Service;

import com.google.cloud.Timestamp;
import java.time.Instant;
import java.util.List;

import com.dev.zine.dao.BlogCategoryDAO;
import com.dev.zine.dao.BlogComponentDAO;
import com.dev.zine.dao.BlogDAO;
import com.dev.zine.model.BlogCategory;
import com.dev.zine.model.BlogComponent;
import com.dev.zine.model.Blog;

import com.dev.zine.api.model.blogs.BlogBody;
import com.dev.zine.api.model.blogs.BlogComponentBody;
import com.dev.zine.exceptions.CategoryAlreadyExists;



@Service
public class BlogService {
    private BlogCategoryDAO blogCategoryDAO;
    private BlogDAO blogDAO;
    private BlogComponentDAO blogComponentDAO;
    public BlogService(BlogCategoryDAO blogCategoryDAO, BlogDAO blogDAO, BlogComponentDAO blogComponentDAO){
        this.blogCategoryDAO = blogCategoryDAO;
        this.blogDAO = blogDAO;
        this.blogComponentDAO = blogComponentDAO;
    }
    public Boolean createCategory(String category_name) throws CategoryAlreadyExists{
        try{
            if(blogCategoryDAO.findByCategoryIgnoreCase(category_name).isPresent()){
                throw new CategoryAlreadyExists();
            }
            BlogCategory newCategory = new BlogCategory();
            newCategory.setCategory(category_name);
            blogCategoryDAO.save(newCategory);
            
            return true;
        } catch(Exception e){
            System.out.println("Unable to create" + e.getMessage());
            return false;
        }
    }
    public Long createBlog(BlogBody blogBody) throws Exception{
        try{
            Blog newBlog = new Blog();
            BlogCategory categoryBelongs = blogCategoryDAO.findByCategoryID(blogBody.getBlogCategory());
            newBlog.setBlogName(blogBody.getBlog_name());
            newBlog.setBlogDescription(blogBody.getBlog_description());
            newBlog.setCreatedAt(Timestamp.ofTimeSecondsAndNanos(Instant.now().getEpochSecond(), Instant.now().getNano()));
            newBlog.setDpURL(blogBody.getDp_url());
            newBlog.setBlogCategory(categoryBelongs);
            blogDAO.save(newBlog);
            return newBlog.getBlogID();
        } catch(Exception e){
            throw new Exception("Failed to create blog", e);
        }
        
    }
    public void createBlogComponent(BlogComponentBody blogComponentBody) throws Exception{
        BlogComponent newComponent = new BlogComponent();
        Blog parentBlog = blogDAO.findByBlogID(blogComponentBody.getBlog_id());
        newComponent.setComponentType(blogComponentBody.getComponent_type());
        newComponent.setContent(blogComponentBody.getContent());
        newComponent.setRelativeOrder(blogComponentBody.getOrder());
        newComponent.setBlog(parentBlog);
        blogComponentDAO.save(newComponent);
        // try {
        // } catch(Exception e){
        //     throw new Exception("Could not create component");
        // }
    }
    public List<BlogComponent> getBlogComponents(Long BlogID) throws Exception{
        Blog parentBlog = blogDAO.findByBlogID(BlogID);
        return blogComponentDAO.findByBlog(parentBlog);
        // try {
        // } catch(Exception e){
        //     throw new Exception("Could not create component");
        // }
    }
    
}
