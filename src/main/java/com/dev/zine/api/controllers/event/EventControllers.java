package com.dev.zine.api.controllers.event;

import com.dev.zine.api.model.event.EventBody;
import com.dev.zine.api.model.event.EventsList;
import com.dev.zine.api.model.event.HackathonRegistrationsBody;
import com.dev.zine.exceptions.EventNotFound;
import com.dev.zine.exceptions.StageNotFound;
import com.dev.zine.model.Event;
import com.dev.zine.service.EventService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/event")
public class EventControllers {
    @Autowired
    private EventService eventService;

    @GetMapping()
    public ResponseEntity<?> getAllEvents() {
        try {
            List<Event> recs = eventService.getAllEvents();
            return ResponseEntity.ok().body(Map.of("events", recs));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(@PathVariable Long id){
        try {
            Event rec = eventService.getEvent(id);
            return ResponseEntity.ok().body(Map.of("event",rec));
        } catch(EventNotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping()
    public ResponseEntity<?> createEvent(@Valid @RequestBody EventBody event) {
        try {
            Event rec = eventService.createEvent(event);
            return ResponseEntity.ok().body(Map.of("event", rec));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody EventBody event) {
        try {
            Event rec = eventService.updateEvent(id ,event);
            return ResponseEntity.ok().body(Map.of("event",rec));
        } catch(StageNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch(EventNotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteEvent(@RequestBody EventsList ids){
        try{
            eventService.deleteEvents(ids.getEventIds());
            return ResponseEntity.ok().build();
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/hackathon/registration-info")
    public ResponseEntity<?> getRegistrationInfo() {
        try {
            HackathonRegistrationsBody response = eventService.getHackthonRegistrations();
            return ResponseEntity.ok().body(response);
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage()));
        }
    }
    
}
