package com.dev.zine.api.controllers.instance;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dev.zine.api.model.task.CheckpointCreateBody;
import com.dev.zine.api.model.task.CheckpointResBody;
import com.dev.zine.api.model.task.LinkCreateBody;
import com.dev.zine.api.model.task.LinkResBody;
import com.dev.zine.exceptions.TaskInstanceNotFound;
import com.dev.zine.exceptions.UserNotFound;
import com.dev.zine.model.TaskInstance;
import com.dev.zine.service.CheckpointsService;
import com.dev.zine.service.LinksService;
import com.dev.zine.service.TaskService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/instance")
public class InstanceController {
    @Autowired
    private CheckpointsService checkpointsService;
    @Autowired
    private LinksService linksService;
    @Autowired
    private TaskService taskService;

    @GetMapping()
    public ResponseEntity<?> getAllInstances() {
        try {
            List<TaskInstance> allInstances = taskService.getEveryInstance();
            return ResponseEntity.ok().body(Map.of("instances", allInstances));
        } catch(Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/{instanceId}/checkpoints")
    public ResponseEntity<?> getCheckpoints(@PathVariable Long instanceId) {
        try {
            List<CheckpointResBody> checkpoints = checkpointsService.getCheckpoints(instanceId);
            return ResponseEntity.ok().body(Map.of("checkpoints", checkpoints));
        } catch (TaskInstanceNotFound e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/{instanceId}/checkpoints")
    public ResponseEntity<?> addCheckpoint(@RequestBody CheckpointCreateBody body, @PathVariable Long instanceId) {
        try {
            CheckpointResBody checkpoint = checkpointsService.addCheckpoint(instanceId, body);
            return ResponseEntity.ok().body(Map.of("checkpoint", checkpoint));
        } catch (TaskInstanceNotFound | UserNotFound e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{instanceId}/links")
    public ResponseEntity<?> getLinks(@PathVariable Long instanceId) {
        try {
            List<LinkResBody> links = linksService.getLinks(instanceId);
            return ResponseEntity.ok().body(Map.of("links", links));
        } catch (TaskInstanceNotFound e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/{instanceId}/links")
    public ResponseEntity<?> addLink(@RequestBody LinkCreateBody body, @PathVariable Long instanceId) {
        try {
            LinkResBody link = linksService.addLink(instanceId, body);
            return ResponseEntity.ok().body(Map.of("link", link));
        } catch (TaskInstanceNotFound | UserNotFound e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
}
