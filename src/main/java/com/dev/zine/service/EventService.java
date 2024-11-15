package com.dev.zine.service;

import com.dev.zine.model.Event;
import com.dev.zine.model.HackathonRegistrations;
import com.dev.zine.model.Recruitment;
import com.dev.zine.utils.NullAwareBeanUtilsBean;
import com.dev.zine.api.model.event.EventBody;
import com.dev.zine.api.model.event.HackathonRegistrationsBody;
import com.dev.zine.api.model.user.UserResponseBody;
import com.dev.zine.dao.EventDAO;
import com.dev.zine.dao.HackathonRegistrationDAO;
import com.dev.zine.dao.RecruitmentDAO;
import com.dev.zine.exceptions.EventNotFound;
import com.dev.zine.exceptions.RecruitmentNotFound;
import com.dev.zine.exceptions.StageNotFound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
    @Autowired
    private EventDAO eventDAO;
    @Autowired
    private RecruitmentDAO recruitmentDAO;
    @Autowired
    private HackathonRegistrationDAO hackathonDAO;

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

    public HackathonRegistrationsBody getHackthonRegistrations() {
        List<HackathonRegistrations> registrations = hackathonDAO.findAll();
        List<UserResponseBody> list =  registrations.stream().map(register -> {
            UserResponseBody body = new UserResponseBody();
            body.setEmail(register.getUserId().getEmail());
            body.setName(register.getUserId().getName());
            return body;
        }).collect(Collectors.toList());
        HackathonRegistrationsBody response = new HackathonRegistrationsBody();
        response.setList(list);
        response.setNumRegistrations(list.size());
        return response;
    }
}
