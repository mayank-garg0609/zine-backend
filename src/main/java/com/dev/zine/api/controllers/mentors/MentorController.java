package com.dev.zine.api.controllers.mentors;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.zine.api.model.mentors.MentorUpdateBody;
import com.dev.zine.exceptions.TaskNotFoundException;
import com.dev.zine.service.MentorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/mentor")
public class MentorController {
    @Autowired
    private MentorService mentorService;

    @PostMapping("/add")
    public ResponseEntity<?> addMentors(@RequestBody MentorUpdateBody update) throws TaskNotFoundException{
        try{
            mentorService.addMentors(update);
            return ResponseEntity.ok().build();
        } catch(TaskNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
}
