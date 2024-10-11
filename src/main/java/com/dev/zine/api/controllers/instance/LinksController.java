package com.dev.zine.api.controllers.instance;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dev.zine.api.model.task.LinkCreateBody;
import com.dev.zine.exceptions.LinkNotFound;
import com.dev.zine.model.InstanceLinks;
import com.dev.zine.service.LinksService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequestMapping("/links")
public class LinksController {
    @Autowired
    private LinksService linksService;

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCheckpoint(@PathVariable Long id, @RequestBody LinkCreateBody body) {
        try {
            InstanceLinks updated = linksService.updateLink(id, body);
            return ResponseEntity.ok().body(Map.of("Links", updated));
        } catch(LinkNotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCheckpoint(@PathVariable Long id) {
        try {
            linksService.deleteLink(id);
            return ResponseEntity.ok().build();
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }
}
