package com.dev.zine.api.controllers.roomMembers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.zine.api.model.Room.RoomBody;
import com.dev.zine.api.model.roomMembers.AddMembersBody;
import com.dev.zine.exceptions.UserAlreadyExistsException;
import com.dev.zine.model.Rooms;
import com.dev.zine.service.RoomMembersService;
import com.dev.zine.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/members")
public class RoomMembersController {
    private RoomMembersService roomMembersService;

    public RoomMembersController(RoomMembersService roomMembersService) {
        this.roomMembersService = roomMembersService;
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody AddMembersBody addMembersBody) {
        System.out.println("addMembersBody");
        System.out.println(addMembersBody);
        try {
            roomMembersService.AddMembers();
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/get")
    public ResponseEntity getRoomMembers(@RequestParam Long roomId) {
        System.out.println(roomId);
        try {
            roomMembersService.getMemebers();

            return ResponseEntity.ok().build();

        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
