package com.dev.zine.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dev.zine.api.model.Room.RoomBody;
import com.dev.zine.dao.RoomMembersDAO;
import com.dev.zine.dao.RoomsDAO;
import com.dev.zine.model.RoomMembers;
import com.dev.zine.model.Rooms;

@Service
public class RoomService {

    private RoomsDAO roomDAO;
    private RoomMembersDAO roomMembersDAO;

    public RoomService(RoomsDAO roomsDAO, RoomMembersDAO roomMembersDAO) {
        this.roomDAO = roomsDAO;
        this.roomMembersDAO = roomMembersDAO;
    }

    public Rooms createRoom(RoomBody room) {
        System.out.println("tried to accessss");
        Rooms newRoom = new Rooms();
        newRoom.setName(room.getName());
        newRoom.setType(room.getType());
        newRoom.setDescription(room.getDescription());
        newRoom.setDpUrl(room.getDpUrl());
        roomDAO.save(newRoom);
        return newRoom;

    }

    public Optional<Rooms> getRoomInfo(Long id) {

        return roomDAO.findById(id);

    }

    public void deleteRoom() {

    }

    public void updateRoomInfo() {

    }

}
