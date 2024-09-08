package com.dev.zine.api.controllers.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.zine.api.model.user.RoomLastSeenInfo;
import com.dev.zine.api.model.user.TokenUpdateBody;
import com.dev.zine.exceptions.RoomDoesNotExist;
import com.dev.zine.exceptions.UserNotFound;
import com.dev.zine.service.UserLastSeenService;
import com.dev.zine.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserLastSeenService lastSeenService;

    @PutMapping("/token")
    public ResponseEntity<?> fcmUpdate(@RequestBody TokenUpdateBody body) {
        try{
            userService.updateTokenHelper(body);
            return ResponseEntity.ok().build();
        } catch(UserNotFound e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{userEmail}/{roomId}/seen")
    public ResponseEntity<?> updateLastSeen(@PathVariable String userEmail, @PathVariable Long roomId) {
        try {
            lastSeenService.updateLastSeen(userEmail, roomId);
            return ResponseEntity.ok().build();
        } catch(UserNotFound | RoomDoesNotExist e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{userEmail}/{roomId}/last-seen")
    public ResponseEntity<?> getLastSeen(@PathVariable String userEmail, @PathVariable Long roomId) {
        try {
            RoomLastSeenInfo lastSeen = lastSeenService.getRoomLastSeenInfo(userEmail, roomId);
            return ResponseEntity.ok().body(Map.of("info", lastSeen));
        } catch(UserNotFound | RoomDoesNotExist e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
}

