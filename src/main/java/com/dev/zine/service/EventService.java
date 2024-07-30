package com.dev.zine.service;

import com.dev.zine.api.model.event.Event;
import com.dev.zine.dao.EventDAO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventDAO eventDAO;

    public List<Event> readAllEvents() {
        List<com.dev.zine.model.Event> eventsList = eventDAO.findAll();
        List<Event> events = new ArrayList<>();

        for(com.dev.zine.model.Event event : eventsList) {

            Event newEvent = new Event();
            newEvent.setId(event.getId());
            newEvent.setDescription(event.getDescription());
            newEvent.setType(event.getType());
            newEvent.setName(event.getName());
            newEvent.setVenue(event.getVenue());
           // newEvent.setRecruitment(event.getRecruitment());
            newEvent.setEnd_date_time(event.getEnd_date_time());
            newEvent.setEnd_date_time(event.getEnd_date_time());

            events.add(newEvent);
        }
        return events;
    }

    public Event readEvent(Long id) {
        com.dev.zine.model.Event even = eventDAO.findById(id).get();
        Event event =new Event();
        BeanUtils.copyProperties(even,event);
        return event;
    }

    public String createEvent(Event event) {
        com.dev.zine.model.Event newEvent = new com.dev.zine.model.Event();
        BeanUtils.copyProperties(event,newEvent);
        eventDAO.save(newEvent);
        return "Event Created";
    }

    public String updateEvent(long id ,Event event) {
        com.dev.zine.model.Event existingEvent = eventDAO.findById(id).get();
        existingEvent.setDescription(event.getDescription());
        existingEvent.setType(event.getType());
        existingEvent.setName(event.getName());
        existingEvent.setVenue(event.getVenue());
      //existingEvent.setRecruitment(event.getRecruitment());
        existingEvent.setStart_date_time(event.getStart_date_time());
        existingEvent.setEnd_date_time(event.getEnd_date_time());

        eventDAO.save(existingEvent);

        return "Event Updated";
    }

    public String deleteEvent(List<Long> ids) {
        eventDAO.deleteAllById(ids);
        return "Event Deleted"+ids ;
    }
}
