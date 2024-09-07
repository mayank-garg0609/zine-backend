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

import com.dev.zine.api.model.room.RoomBody;
import com.dev.zine.exceptions.RoomDoesNotExist;
import com.dev.zine.model.Rooms;
import com.dev.zine.service.RoomMembersService;
import com.dev.zine.service.RoomService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    private RoomService roomService;
    private RoomMembersService roomMembersService;

    public RoomController(RoomService roomService, RoomMembersService roomMembersService) {
        this.roomService = roomService;
        this.roomMembersService = roomMembersService;
    }

    @PostMapping("/create")
    public ResponseEntity createRoom(@Valid @RequestBody RoomBody room) {
        System.out.println(room);
        try {
            Rooms newRoom = roomService.createRoom(room);
            return ResponseEntity.ok().body(newRoom);
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

    @GetMapping("/get-all")
    public ResponseEntity<List<Rooms>> getRoom() {
        try{
            List<Rooms> rooms = roomService.getAllRooms();
            return ResponseEntity.ok().body(rooms);
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<Rooms>> getRoomsByUser(@RequestParam String email) {
        try{
            return roomMembersService.getRoomsByEmail(email);
        } catch( Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    

    @PostMapping("/delete")

    public ResponseEntity delete(@RequestBody List<Long> roomId) {
        System.out.println(roomId);
        try {
            System.out.println("hi1");
                roomService.deleteRooms(roomId);

            return ResponseEntity.ok().body("Rooms Deleted " + roomId.toString());

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("hi2");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/update")
    public ResponseEntity delete(@RequestParam Long roomId, @RequestBody RoomBody room) {
        System.out.println(roomId);
        try {
            roomService.updateRoomInfo(roomId, room);

            return ResponseEntity.ok().body("Rooms Updated " + roomId.toString());

        } catch (RoomDoesNotExist ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room does not exist");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
