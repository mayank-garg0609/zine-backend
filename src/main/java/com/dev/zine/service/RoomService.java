package com.dev.zine.service;

import java.sql.Timestamp;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dev.zine.api.model.room.RoomBody;
import com.dev.zine.api.model.room.RoomResBody;
import com.dev.zine.dao.MessagesDAO;
import com.dev.zine.dao.RoomsDAO;
import com.dev.zine.dao.UserDAO;
import com.dev.zine.exceptions.RoomDoesNotExist;
import com.dev.zine.exceptions.UserNotFound;
import com.dev.zine.model.Message;
import com.dev.zine.model.Rooms;
import com.dev.zine.model.User;

import jakarta.transaction.Transactional;

@Service
public class RoomService {
    @Autowired
    private RoomsDAO roomDAO;
    @Autowired
    private UserLastSeenService userLastSeenService;
    @Autowired
    private MessagesDAO messagesDAO;
    @Autowired
    private UserDAO userDAO;

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

    public RoomResBody getAnnouncementInfo(String email) throws RoomDoesNotExist, UserNotFound{
        List<Rooms> announcementRoomList = roomDAO.findByType("announcement");
        if(announcementRoomList.size()==0 || announcementRoomList.size()>1){
            throw new RoomDoesNotExist();
        }
        User user = userDAO.findByEmailIgnoreCase(email).orElseThrow(() -> new UserNotFound());
        Rooms announcementRoom = announcementRoomList.get(0);
        Timestamp lastSeen = userLastSeenService.getLastSeen(user, announcementRoom);
        Timestamp lastMessageTimestamp = messagesDAO.findFirstByRoomIdOrderByTimestampDesc(announcementRoom).map(Message::getTimestamp).orElse(null);
        Long unreadMessages = messagesDAO.countUnreadMessages(announcementRoom, lastSeen);
        RoomResBody body = new RoomResBody();
        body.setRoom(announcementRoom);
        body.setLastMessageTimestamp(lastMessageTimestamp);
        body.setUnreadMessages(unreadMessages);
        body.setUserLastSeen(lastSeen);
        return body;
    } 
}
