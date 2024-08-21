package com.dev.zine.api.controllers.recruitment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.zine.api.model.recruitment.RecruitmentCreateBody;
import com.dev.zine.api.model.recruitment.RecruitmentListBody;
import com.dev.zine.exceptions.RecruitmentNotFound;
import com.dev.zine.model.Recruitment;
import com.dev.zine.service.RecruitmentService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;
import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/recruitment")
public class RecruitmentController {
    @Autowired
    private RecruitmentService recruitmentService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecruitment(@PathVariable Long id) throws RecruitmentNotFound{
        try {
            Recruitment rec = recruitmentService.getRecruitment(id);
            return ResponseEntity.ok().body(rec);
        } catch(RecruitmentNotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping()
    public ResponseEntity<?> getRecruitments() {
        try {
            List<Recruitment> recs = recruitmentService.getAllRecruitments();
            return ResponseEntity.ok().body(Map.of("recruitments", recs));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecruitment(@PathVariable Long id, @RequestBody RecruitmentCreateBody body) {
        try {
            Recruitment rec = recruitmentService.editRecruitment(id, body);
            return ResponseEntity.ok().body(Map.of("recruitment", rec));
        } catch(RecruitmentNotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping()
    public ResponseEntity<?> createRecruitment(@RequestBody RecruitmentCreateBody body) {
        try {
            Recruitment rec = recruitmentService.createRecruitment(body);
            return ResponseEntity.ok().body(Map.of("recruitment", rec));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteRecruitment(@RequestBody RecruitmentListBody body) {
        try{
            recruitmentService.deleteRecruitment(body.getRecruitments());
            return ResponseEntity.ok().build();
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
