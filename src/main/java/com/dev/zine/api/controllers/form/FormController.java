package com.dev.zine.api.controllers.form;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.zine.api.model.form.creation.FormCreateBody;
import com.dev.zine.api.model.form.form_response.FormResponseBody;
import com.dev.zine.exceptions.EventNotFound;
import com.dev.zine.exceptions.FormIsClosed;
import com.dev.zine.exceptions.NotFoundException;
import com.dev.zine.model.User;
import com.dev.zine.model.form.Form;
import com.dev.zine.service.FormService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/form")
public class FormController {
    @Autowired
    private FormService formService;
    
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping()
    public ResponseEntity<?> getAllForms() {
        return ResponseEntity.ok().body(Map.of("forms", formService.getAllForms()));
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getForm(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(Map.of("form", formService.getForm(id)));
        } catch(NotFoundException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping()
    public ResponseEntity<?> createForm(@RequestBody FormCreateBody body) {
        try {
            Form form = formService.createForm(body);
            return ResponseEntity.ok().body(Map.of("form", form));
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteForm(@PathVariable Long id) {
        try {
            formService.deleteForm(id);
            return ResponseEntity.ok().build();
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/{id}/responses")
    public ResponseEntity<?> addResponse(@PathVariable Long id, @RequestBody List<FormResponseBody> response, @AuthenticationPrincipal User user) {
        try {
            formService.addResponse(id, response, user);
            return ResponseEntity.ok().build();
        } catch(NotFoundException | FormIsClosed e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/{id}/responses")
    public ResponseEntity<?> getResponses(@PathVariable Long id) {
        try {
            String res = formService.getResponses(id);
            return ResponseEntity.ok().body(Map.of("csv", res));
        } catch(NotFoundException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/unfilled")
    public ResponseEntity<?> getUserForms(@AuthenticationPrincipal User user) {
        try {
            return ResponseEntity.ok().body(Map.of("forms", formService.getUserForms(user)));
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('admin')")
    @PutMapping("/{id}")
    public ResponseEntity<?> getUserForms(@PathVariable Long id, @RequestBody FormCreateBody body) {
        try {
            return ResponseEntity.ok().body(Map.of("form", formService.updateForm(id, body)));
        } catch(NotFoundException | EventNotFound e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
