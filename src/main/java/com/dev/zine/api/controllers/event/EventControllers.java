package com.dev.zine.api.controllers.event;

import com.dev.zine.api.model.event.Event;
import com.dev.zine.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventControllers {

    @Autowired
    EventService eventService;

    @GetMapping("/get")
    public List<Event> getAllEvents() {return eventService.readAllEvents();}

    @GetMapping("/get/{id}")
    public Event getEvent(@PathVariable Long id){return eventService.readEvent(id);}

    @PostMapping("/create")
    public String createEvent(@RequestBody Event event) {return eventService.createEvent(event);}

    @PostMapping("/update/{id}")
    public String updateEvent(@PathVariable Long id,@RequestBody Event event) {return eventService.updateEvent(id ,event);}

    @PostMapping("/delete")
    public String deleteEvent(@RequestBody List<Long> ids){return eventService.deleteEvent(ids);}
}
