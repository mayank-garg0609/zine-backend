package com.dev.zine.websocket.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ActiveUsersService {
    private final Map<String, String> connectedUsers = new ConcurrentHashMap<>(); 
    private final Map<String, Set<String>> roomUsers = new ConcurrentHashMap<>(); 

    public void addConnectedUser(String email, String sessionId) {
        connectedUsers.put(sessionId, email);
    }

    public String getEmail(String sessionId) {
        return connectedUsers.get(sessionId);
    }

    public void addUserToRoom(String roomId, String sessionId) {
        roomUsers.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(sessionId);
    }

    public String removeUser(String sessionId) {
        connectedUsers.remove(sessionId);
    
        for (Map.Entry<String, Set<String>> entry : roomUsers.entrySet()) {
            String roomId = entry.getKey();
            Set<String> users = entry.getValue();

            if (users.remove(sessionId)) { 
                if (users.isEmpty()) {
                    roomUsers.remove(roomId); 
                }
                return roomId; 
            }
        }
        return null; 
    }

public Set<String> removeUserFromAllRooms(String sessionId) {
    Set<String> affectedRoomIds = new HashSet<>();

    roomUsers.forEach((roomId, users) -> {
        if (users.remove(sessionId)) { 
            affectedRoomIds.add(roomId);
        }
    });

    roomUsers.entrySet().removeIf(entry -> entry.getValue().isEmpty());

    return affectedRoomIds;
}

    
    public Set<String> getActiveUserEmails(String roomId) {
        return roomUsers.getOrDefault(roomId, Collections.emptySet()).stream()
                .map(connectedUsers::get) 
                .filter(Objects::nonNull) 
                .collect(Collectors.toSet());
    }
        
}

