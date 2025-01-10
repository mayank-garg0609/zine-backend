package com.dev.zine.websocket.event;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.dev.zine.exceptions.MissingAuthorizationToken;
import com.dev.zine.service.JWTService;
import com.dev.zine.websocket.service.ActiveUsersService;

@Component
public class WebSocketEventListener {
    @Autowired SimpMessagingTemplate simpMessagingTemplate;
    @Autowired JWTService jwtService;
    @Autowired ActiveUsersService activeUsersService;

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) throws MissingAuthorizationToken {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String authToken = headers.getFirstNativeHeader("Authorization");
        String sessionId = headers.getSessionId();

        if (authToken == null) throw new MissingAuthorizationToken();

        String email = jwtService.getEmail(authToken);
        activeUsersService.addConnectedUser(email, sessionId);

        System.out.println("User connected: " + email);
    }
    
    @EventListener
    public void handleSessionDisconnected(SessionDisconnectEvent event) {
        String roomId = activeUsersService.removeUser(event.getSessionId());
        broadcastActiveUsers(roomId);
    }

    @EventListener
    public void handleSubscriptionEvent(SessionSubscribeEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String roomId = headers.getFirstNativeHeader("roomId");
        String sessionId = headers.getSessionId();

        if (roomId != null) {
            Set<String> roomIds = activeUsersService.removeUserFromAllRooms(sessionId);
            for(String rem: roomIds) {
                broadcastActiveUsers(rem);
            }
            activeUsersService.addUserToRoom(roomId, sessionId); 
            Set<String> activeUserEmails = activeUsersService.getActiveUserEmails(roomId);
            System.out.println("Active users in room " + roomId + ": " + activeUserEmails);
            broadcastActiveUsers(roomId); 
        }
    }

    private void broadcastActiveUsers(String roomId) {
        simpMessagingTemplate.convertAndSend("/room/"+roomId+"/active-users", 
            activeUsersService.getActiveUserEmails(roomId));
    }
}
