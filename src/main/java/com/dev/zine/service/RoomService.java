package com.dev.zine.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dev.zine.api.model.room.RoomBody;
import com.dev.zine.dao.RoomMembersDAO;
import com.dev.zine.dao.RoomsDAO;
import com.dev.zine.exceptions.RoomDoesNotExist;
import com.dev.zine.model.Rooms;

import jakarta.transaction.Transactional;

@Service
public class RoomService {

    private RoomsDAO roomDAO;
    private RoomMembersDAO roomMembersDAO;

    @Autowired
    public RoomService(RoomsDAO roomsDAO, RoomMembersDAO roomMembersDAO) {
        this.roomDAO = roomsDAO;
        this.roomMembersDAO = roomMembersDAO;
    }

    public Rooms createRoom(RoomBody room) {
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

    public List<Rooms> getAllRooms() {

        return roomDAO.findAll();

    }
    @Transactional
    public void deleteRooms(List<Long> ids) {
        roomDAO.deleteAllById(ids);

    }

    public Rooms updateRoomInfo(Long roomId, RoomBody room) throws RoomDoesNotExist {
        Rooms existingRoom = roomDAO.findById(roomId).orElse(null);

        if (existingRoom != null) {
            try {
                if (room.getName() != null) {
                    existingRoom.setName(room.getName());
                }
                if (room.getType() != null) {
                    existingRoom.setType(room.getType());
                }
                if (room.getDescription() != null) {
                    existingRoom.setDescription(room.getDescription());
                }
                if (room.getDpUrl() != null) {
                    existingRoom.setDpUrl(room.getDpUrl());
                }

                return roomDAO.save(existingRoom);
            } catch (Exception ex) {

                throw ex;
            }
        } else {

            throw new RoomDoesNotExist();
        }
    }

    public Rooms getAnnouncementInfo() throws RoomDoesNotExist{
        List<Rooms> announcementRoom = roomDAO.findByType("announcement");
        if(announcementRoom.size()==0 || announcementRoom.size()>1){
            throw new RoomDoesNotExist();
        }
        return announcementRoom.get(0);
    } 
}
