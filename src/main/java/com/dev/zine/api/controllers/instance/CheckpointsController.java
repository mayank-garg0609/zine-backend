package com.dev.zine.api.controllers.instance;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dev.zine.api.model.task.CheckpointCreateBody;
import com.dev.zine.exceptions.CheckpointNotFound;
import com.dev.zine.model.InstanceCheckpoints;
import com.dev.zine.service.CheckpointsService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequestMapping("/checkpoints")
public class CheckpointsController {
    @Autowired
    private CheckpointsService checkpointsService;

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCheckpoint(@PathVariable Long id, @RequestBody CheckpointCreateBody body) {
        try {
            InstanceCheckpoints updated = checkpointsService.updateCheckpoint(id, body);
            return ResponseEntity.ok().body(Map.of("checkpoint", updated));
        } catch(CheckpointNotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCheckpoint(@PathVariable Long id) {
        try {
            checkpointsService.deleteCheckpoint(id);
            return ResponseEntity.ok().build();
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }
}
