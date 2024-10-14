package com.dev.zine.api.controllers.comments;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.zine.api.model.comments.CommentCreateBody;
import com.dev.zine.api.model.comments.CommentResponse;
import com.dev.zine.exceptions.CommentNotFound;
import com.dev.zine.exceptions.TaskInstanceNotFound;
import com.dev.zine.exceptions.UserNotFound;
import com.dev.zine.service.InstanceCommentsService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private InstanceCommentsService commentsService;

    @GetMapping()
    public ResponseEntity<?> getInstanceComments(@RequestParam Long instance) {
        try{
            List<CommentResponse> comments = commentsService.getInstanceComments(instance); 
            return ResponseEntity.ok().body(Map.of("comments", comments));

        } catch(TaskInstanceNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping()
    public ResponseEntity<?> postComment(@RequestParam Long instance, @RequestBody CommentCreateBody body) {
        try{
            CommentResponse comment = commentsService.createComment(instance, body); 
            return ResponseEntity.ok().body(Map.of("comment", comment));
        } catch(TaskInstanceNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch(UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @RequestBody CommentCreateBody body) {
        try{
            CommentResponse comment = commentsService.updateComment(id, body); 
            return ResponseEntity.ok().body(Map.of("comment", comment));
        } catch(CommentNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } 
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        try{
            commentsService.deleteComment(id); 
            return ResponseEntity.ok().build();
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        } 
    }
}
