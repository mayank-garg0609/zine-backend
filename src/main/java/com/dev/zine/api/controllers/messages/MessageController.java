package com.dev.zine.api.controllers.messages;

import java.util.List;
import java.util.Map;

import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import com.dev.zine.api.model.messages.creation.MessageCreateBody;
import com.dev.zine.api.model.messages.response.MsgResBody;
import com.dev.zine.exceptions.RoomDoesNotExist;
import com.dev.zine.model.Message;
import com.dev.zine.model.chat.ChatItem;
import com.dev.zine.service.MessagingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// @Controller
@RestController
@RequestMapping("/messages")
public class MessageController {

    private MessagingService messagingService;

    public MessageController(MessagingService messagingService) {
        this.messagingService = messagingService;
    }
    
    @PostMapping("/http-msg")
    public ResponseEntity<?> sendHttpMessage(@RequestBody MessageCreateBody msg) {
        try {
            messagingService.sendMessage(msg);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }
    

    // @MessageMapping("/send")
    // public void sendMessage(MessageBody message) {
    //     try {
    //         messagingService.sendMessage(message);
    //     } catch (Exception ex) {
    //         ex.printStackTrace();
    //     }
    // }

    @GetMapping("/roomMsg")
    public ResponseEntity<?> getRoomMessages(@RequestParam long roomId) {
        try {
            List<MsgResBody> msgs = messagingService.getRoomMessages(roomId);
            return ResponseEntity.ok(msgs);
        } catch (RoomDoesNotExist ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room does not exist");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        }
    }
}