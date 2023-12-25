package com.dev.zine.api.controllers.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.zine.model.NotifyT;
import com.dev.zine.service.FirebaseMessagingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class TestController {

    private final FirebaseMessagingService fcm;

    public TestController(FirebaseMessagingService fcm) {
        this.fcm = fcm;
    }

    @GetMapping("/test")
    public String test() {
        return "API is Working";
    }

    @PostMapping("/sendNotif")
    public String postMethodName(@RequestParam String token, @Valid @RequestBody NotifyT note) {
        try {
            fcm.sendNotifications(token, note);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send notification";
        }
        return "Notification sent successfully";
    }
}
