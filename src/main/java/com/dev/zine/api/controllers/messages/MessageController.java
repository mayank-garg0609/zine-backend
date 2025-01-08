package com.dev.zine.api.controllers.messages;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;

import com.dev.zine.api.model.messages.creation.MessageCreateBody;
import com.dev.zine.api.model.messages.response.MsgResBody;
import com.dev.zine.exceptions.NotFoundException;
import com.dev.zine.exceptions.RoomDoesNotExist;
import com.dev.zine.model.User;
import com.dev.zine.service.MessagingService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private MessagingService messagingService;

    public MessageController(MessagingService messagingService) {
        this.messagingService = messagingService;
    }
    
    @PostMapping()
    public ResponseEntity<?> sendHttpMessage(@RequestBody MessageCreateBody msg) {
        try {
            messagingService.sendMessage(msg);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> editMessage(@PathVariable Long id, @RequestBody MessageCreateBody msg, @AuthenticationPrincipal User user) {
        try {
            messagingService.editMessage(id, msg, user);
            return ResponseEntity.ok().build();
        } catch(NotFoundException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long id, @AuthenticationPrincipal User user) {
        try {
            messagingService.deleteMessage(id, user);
            return ResponseEntity.ok().build();
        } catch(NotFoundException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/roomMsg")
    public ResponseEntity<?> getRoomMessages(@RequestParam long roomId, @AuthenticationPrincipal User user) {
        try {
            List<MsgResBody> msgs = messagingService.getRoomMessages(roomId, user);
            return ResponseEntity.ok(msgs);
        } catch (RoomDoesNotExist ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room does not exist");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        }
    }
}