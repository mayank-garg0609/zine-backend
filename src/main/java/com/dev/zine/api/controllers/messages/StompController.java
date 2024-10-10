package com.dev.zine.api.controllers.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.dev.zine.api.model.messages.MessageBody;
import com.dev.zine.model.Message;
import com.dev.zine.service.MessagingService;

@Controller
public class StompController {

    @Autowired
    private MessagingService messagingService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    
    @MessageMapping("/message")
    public void sendStompMessage(@Payload MessageBody msg) {
        try {
            Message message = messagingService.sendMessage(msg);
            simpMessagingTemplate.convertAndSend("/room/" + msg.getRoomId(),
                message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}