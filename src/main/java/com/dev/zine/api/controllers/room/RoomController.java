package com.dev.zine.api.controllers.room;

import java.util.List;
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
import com.dev.zine.model.Rooms;
import com.dev.zine.service.RoomService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    private RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/create")
    public ResponseEntity createRoom(@Valid @RequestBody RoomBody room) {
        System.out.println(room);
        try {
            roomService.createRoom(room);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/get")
    public ResponseEntity getRoom(@RequestParam Long roomId) {
        System.out.println(roomId);
        try {
            Optional<Rooms> room = roomService.getRoomInfo(roomId);
            if (room.isPresent())
                return ResponseEntity.ok(room);
            else
                return ResponseEntity.notFound().build();

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/delete")
    public ResponseEntity delete(@RequestBody List<Long> roomId) {
        System.out.println(roomId);
        try {
            roomService.deleteRooms(roomId);

            return ResponseEntity.ok().body("Rooms Deleted " + roomId.toString());

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
