package com.dev.zine.service;

import com.dev.zine.model.Event;
import com.dev.zine.model.Recruitment;
import com.dev.zine.utils.NullAwareBeanUtilsBean;
import com.dev.zine.api.model.event.EventBody;
import com.dev.zine.dao.EventDAO;
import com.dev.zine.dao.RecruitmentDAO;
import com.dev.zine.exceptions.EventNotFound;
import com.dev.zine.exceptions.RecruitmentNotFound;
import com.dev.zine.exceptions.StageNotFound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Service
public class EventService {
    @Autowired
    private EventDAO eventDAO;
    @Autowired
    private RecruitmentDAO recruitmentDAO;

    public List<Event> getAllEvents() {
        return eventDAO.findAll();
    }

    public Event getEvent(Long id) throws EventNotFound{
        Event event = eventDAO.findById(id).orElseThrow(() -> new EventNotFound(id));
        return event;
    }

    public Event createEvent(EventBody body) throws RecruitmentNotFound, StageNotFound {
        Event newEvent = new Event();
        if(body.getRecruitment() != null){
            Recruitment rec = recruitmentDAO.findByStage(body.getRecruitment());
            if(rec == null) {
                throw new StageNotFound(body.getRecruitment());
            }
            newEvent.setRecruitment(rec);
        }
        newEvent.setDescription(body.getDescription());
        newEvent.setEndDateTime(body.getEndDateTime());
        newEvent.setStartDateTime(body.getStartDateTime());
        newEvent.setName(body.getName());
        newEvent.setType(body.getType());
        newEvent.setVenue(body.getVenue());
        eventDAO.save(newEvent);
        return newEvent;
    }

    public Event updateEvent(long id, EventBody update) throws EventNotFound, StageNotFound {
        Event existing = eventDAO.findById(id).orElseThrow(() -> new EventNotFound(id));
        try{
            NullAwareBeanUtilsBean beanUtilsBean = new NullAwareBeanUtilsBean();
            beanUtilsBean.copyProperties(existing, update);
            if(update.getRecruitment() != null){
                Recruitment rec = recruitmentDAO.findByStage(update.getRecruitment());
                if(rec == null) {
                    throw new StageNotFound(update.getRecruitment());
                }
                existing.setRecruitment(rec);
            }
            eventDAO.save(existing);
            return existing;
        } catch(IllegalAccessException | InvocationTargetException e){
            return existing;
        }
    }

    public void deleteEvents(List<Long> ids) {
        eventDAO.deleteAllById(ids);
    }
}
