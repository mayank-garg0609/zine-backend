package com.dev.zine.api.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.zine.api.model.user.TokenUpdateBody;
import com.dev.zine.exceptions.UserNotFound;
import com.dev.zine.service.UserService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PutMapping("/token")
    public ResponseEntity<?> fcmUpdate(@RequestBody TokenUpdateBody body) {
        try{
            userService.updateTokenHelper(body);
            return ResponseEntity.ok().build();
        } catch(UserNotFound e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
}
