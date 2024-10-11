package com.dev.zine.api.controllers.instance;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dev.zine.api.model.task.CheckpointCreateBody;
import com.dev.zine.api.model.task.LinkCreateBody;
import com.dev.zine.exceptions.TaskInstanceNotFound;
import com.dev.zine.model.InstanceCheckpoints;
import com.dev.zine.model.InstanceLinks;
import com.dev.zine.service.CheckpointsService;
import com.dev.zine.service.LinksService;
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

    @GetMapping("/{instanceId}/checkpoints")
    public ResponseEntity<?> getCheckpoints(@PathVariable Long instanceId) {
        try {
            List<InstanceCheckpoints> checkpoints = checkpointsService.getCheckpoints(instanceId);
            return ResponseEntity.ok().body(Map.of("checkpoints", checkpoints));
        } catch (TaskInstanceNotFound e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/{instanceId}/checkpoints")
    public ResponseEntity<?> addCheckpoint(@RequestBody CheckpointCreateBody body, @PathVariable Long instanceId) {
        try {
            InstanceCheckpoints checkpoint = checkpointsService.addCheckpoint(instanceId, body);
            return ResponseEntity.ok().body(Map.of("checkpoint", checkpoint));
        } catch (TaskInstanceNotFound e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{instanceId}/links")
    public ResponseEntity<?> getLinks(@PathVariable Long instanceId) {
        try {
            List<InstanceLinks> links = linksService.getLinks(instanceId);
            return ResponseEntity.ok().body(Map.of("links", links));
        } catch (TaskInstanceNotFound e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/{instanceId}/links")
    public ResponseEntity<?> addLink(@RequestBody LinkCreateBody body, @PathVariable Long instanceId) {
        try {
            InstanceLinks link = linksService.addLink(instanceId, body);
            return ResponseEntity.ok().body(Map.of("link", link));
        } catch (TaskInstanceNotFound e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
}
