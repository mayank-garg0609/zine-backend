package com.dev.zine.basanti;

import com.dev.zine.api.model.event.EventBody;
import com.dev.zine.basanti.model.ChatBasanti;
import com.dev.zine.basanti.model.ProjectBasanti;
import com.dev.zine.basanti.model.TaskBasanti;
import com.dev.zine.basanti.model.UserBasanti;
import com.dev.zine.exceptions.EmailNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/basanti")
public class BasantiControllers {

    @Autowired
    private BasantiService basantiService;

    @GetMapping("/user")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserBasanti> users = basantiService.getAllUsers();
            return ResponseEntity.ok().body(Map.of("users", users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/event")
    public ResponseEntity<?> getAllEvents() {
        try {
            List<EventBody> events = basantiService.getAllEvents();
            return ResponseEntity.ok().body(Map.of("events", events));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/project")
    public ResponseEntity<?> getAllProjects() {
        try {
            List<ProjectBasanti> projects = basantiService.getAllProjects();
            return ResponseEntity.ok().body(Map.of("projects", projects));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/chats/{userEmail}")
    public ResponseEntity<?> getUserChats(@PathVariable String userEmail){
        try {
            List<ChatBasanti> chats = basantiService.getUserChats(userEmail);
            return ResponseEntity.ok().body(Map.of("chat",chats));
        } catch(EmailNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/project/{userEmail}")
    public ResponseEntity<?> getUserProjects(@PathVariable String userEmail){
        try {
            List<TaskBasanti> tasks = basantiService.getUserProjects(userEmail);
            return ResponseEntity.ok().body(Map.of("task",tasks));
        } catch(EmailNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

}
