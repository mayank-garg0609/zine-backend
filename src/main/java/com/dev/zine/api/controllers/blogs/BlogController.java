package com.dev.zine.api.controllers.blogs;

import org.springframework.web.bind.annotation.RestController;

import com.dev.zine.api.model.blogs.BlogBody;
import com.dev.zine.api.model.blogs.BlogComponentBody;
import com.dev.zine.dao.BlogDAO;
import com.dev.zine.exceptions.BlogNotFound;
import com.dev.zine.model.Blog;
import com.dev.zine.model.BlogComponent;
import com.dev.zine.service.BlogService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private BlogDAO blogDAO;

    @PostMapping()
    public ResponseEntity<?> createBlog(@RequestBody BlogBody body) {
        try{
            Blog newBlog = blogService.createBlog(body);
            return ResponseEntity.ok().body(Map.of("blog", newBlog));
        } catch(BlogNotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping()
    public ResponseEntity<?> getBlogsByParent(@RequestParam Long id) throws BlogNotFound{
        try{
            List<Blog> blogs = blogService.fetchChildrenBlogs(id);
            return ResponseEntity.ok().body(Map.of("blogs", blogs));
        } catch(BlogNotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    // @PostMapping("/create-blog")
    // public ResponseEntity<?> createBlog(@RequestBody BlogBody blogBody) {
    //     try {
    //         Long blogId = blogService.createBlog(blogBody);
    //         if (blogId != null && blogId > 0) {
    //             // Returning JSON response with blog ID
    //             return ResponseEntity.ok().body(Map.of("message", "Blog created successfully", "blogId", blogId));
    //         } else {
    //             return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Blog creation failed"));
    //         }
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Failed to create blog", "error", e.getMessage()));
    //     }
    // }
    // @PostMapping("/create-component")
    // public ResponseEntity<String> createBlogComponent(@RequestBody BlogComponentBody blogComponentBody) {
    //     try {
    //         blogService.createBlogComponent(blogComponentBody);
    //         return ResponseEntity.ok("Component created successfully");
    //     } catch(Exception e){
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create category"+e.getMessage());
    //     }
    // }
    // @GetMapping("/get-components")
    // public ResponseEntity<?> getBlogComponents(@RequestParam Long blogID) {
    //     try {
    //         List<BlogComponent> components = blogService.getBlogComponents(blogID);
    //         return ResponseEntity.ok(components);
    //     } catch (Exception e) {
    //         // Log the exception (optional)
    //         // logger.error("Failed to get blog components", e);

    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get blog components: " + e.getMessage());
    //     }
    // }
}

