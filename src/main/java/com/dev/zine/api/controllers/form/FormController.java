package com.dev.zine.api.controllers.form;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.zine.api.model.form.creation.FormCreateBody;
import com.dev.zine.api.model.form.form_response.FormResponseBody;
import com.dev.zine.exceptions.NotFoundException;
import com.dev.zine.model.User;
import com.dev.zine.service.FormService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/form")
public class FormController {
    @Autowired
    private FormService formService;
    
    @GetMapping()
    public ResponseEntity<?> getAllForms() {
        return ResponseEntity.ok().body(Map.of("forms", formService.getAllForms()));
    }

    @PostMapping()
    public ResponseEntity<?> createForm(@RequestBody FormCreateBody body) {
        try {
            formService.createForm(body);
            return ResponseEntity.ok().build();
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteForm(@PathVariable Long id) {
        try {
            formService.deleteForm(id);
            return ResponseEntity.ok().build();
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/{id}/response")
    public ResponseEntity<?> addResponse(@PathVariable Long id, @RequestBody List<FormResponseBody> response, @AuthenticationPrincipal User user) {
        try {
            formService.addResponse(id, response, user);
            return ResponseEntity.ok().build();
        } catch(NotFoundException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    
    
}
