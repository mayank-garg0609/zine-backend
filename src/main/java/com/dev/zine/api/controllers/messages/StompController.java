package com.dev.zine.api.controllers.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.dev.zine.api.model.messages.creation.MessageCreateBody;
import com.dev.zine.model.chat.ChatItem;
import com.dev.zine.service.MessagingService;

@Controller
public class StompController {

    @Autowired
    private MessagingService messagingService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    
    @MessageMapping("/message")
    public void sendStompMessage(@Payload MessageCreateBody msg) {
        try {
            ChatItem message = messagingService.sendMessage(msg);
            simpMessagingTemplate.convertAndSend("/room/" + msg.getRoomId(),
                message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}