package com.dev.zine.api.controllers.blogs;

import org.springframework.web.bind.annotation.RestController;

import com.dev.zine.api.model.blogs.BlogBody;
import com.dev.zine.api.model.blogs.BlogsListBody;
import com.dev.zine.api.model.task.TaskListBody;
import com.dev.zine.dao.BlogDAO;
import com.dev.zine.exceptions.BlogNotFound;
import com.dev.zine.model.Blog;
import com.dev.zine.service.BlogService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;

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

    @PutMapping("/{blogId}")
    public ResponseEntity<?> updateBlog(@PathVariable Long blogId, @RequestBody BlogBody body) {
        try{
            Blog updatedBlog = blogService.updateBlog(blogId, body);
            return ResponseEntity.ok().body(Map.of("blog", updatedBlog));
        } catch(BlogNotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteBlog(@RequestBody BlogsListBody body) {
        if(blogService.deleteBlog(body.getBlogIds())){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
    
}

