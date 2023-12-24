package com.dev.zine.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dev.zine.api.model.Room.RoomBody;
import com.dev.zine.dao.RoomMembersDAO;
import com.dev.zine.dao.RoomsDAO;
import com.dev.zine.model.RoomMembers;
import com.dev.zine.model.Rooms;

@Service
public class RoomMembersService {

    private RoomsDAO roomDAO;
    private RoomMembersDAO roomMembersDAO;

    public RoomMembersService(RoomsDAO roomsDAO, RoomMembersDAO roomMembersDAO) {
        this.roomDAO = roomsDAO;
        this.roomMembersDAO = roomMembersDAO;
    }

    public void AddMembers() {

    }

    public void RemoveMembers() {

    }

    public void UpdateMembers() {

    }

    public void getMemebers() {

    }

}
