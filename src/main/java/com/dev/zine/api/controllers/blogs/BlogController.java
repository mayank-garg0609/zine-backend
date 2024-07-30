package com.dev.zine.api.controllers.blogs;

import org.springframework.web.bind.annotation.RestController;

import com.dev.zine.api.model.blogs.BlogBody;
import com.dev.zine.api.model.blogs.BlogComponentBody;
import com.dev.zine.model.BlogComponent;
import com.dev.zine.service.BlogService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/blog")
public class BlogController {
    private BlogService blogService;
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }
    @PostMapping("/create_category")
    public ResponseEntity<String> createCategory(@RequestBody String entity) {
        try{
            Boolean res = blogService.createCategory(entity);
            if(res){
                return ResponseEntity.ok("Category created successfully");
            } else{
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Category already exits");
            }
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create category");
        }
    }
    @PostMapping("/create-blog")
    public ResponseEntity<?> createBlog(@RequestBody BlogBody blogBody) {
        try {
            Long blogId = blogService.createBlog(blogBody);
            if (blogId != null && blogId > 0) {
                // Returning JSON response with blog ID
                return ResponseEntity.ok().body(Map.of("message", "Blog created successfully", "blogId", blogId));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Blog creation failed"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Failed to create blog", "error", e.getMessage()));
        }
    }
    @PostMapping("/create-component")
    public ResponseEntity<String> createBlogComponent(@RequestBody BlogComponentBody blogComponentBody) {
        try {
            blogService.createBlogComponent(blogComponentBody);
            return ResponseEntity.ok("Component created successfully");
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create category"+e.getMessage());
        }
    }
    @GetMapping("/get-components")
    public ResponseEntity<?> getBlogComponents(@RequestParam Long blogID) {
        try {
            List<BlogComponent> components = blogService.getBlogComponents(blogID);
            return ResponseEntity.ok(components);
        } catch (Exception e) {
            // Log the exception (optional)
            // logger.error("Failed to get blog components", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get blog components: " + e.getMessage());
        }
    }
    
    
    
    
}
