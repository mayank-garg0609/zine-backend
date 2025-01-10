package com.dev.zine.api.controllers.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.dev.zine.api.model.messages.PollVoteBody;
import com.dev.zine.api.model.messages.creation.MessageCreateBody;
import com.dev.zine.api.model.messages.response.BroadcastMsgBody;
import com.dev.zine.exceptions.NotFoundException;
import com.dev.zine.exceptions.UserNotFound;
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
            BroadcastMsgBody message = messagingService.sendMessage(msg);
            simpMessagingTemplate.convertAndSend("/room/" + msg.getRoomId(),
                message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @MessageMapping("/poll-vote")
    public void updatePoll(@Payload PollVoteBody vote) {
        try {
            messagingService.registerVote(vote);
        } catch(UserNotFound | NotFoundException e) {
            e.printStackTrace();
        }
    }
}