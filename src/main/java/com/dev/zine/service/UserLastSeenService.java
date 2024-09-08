package com.dev.zine.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.zine.dao.RoomsDAO;
import com.dev.zine.dao.UserDAO;
import com.dev.zine.dao.UserLastSeenDAO;
import com.dev.zine.exceptions.RoomDoesNotExist;
import com.dev.zine.exceptions.UserNotFound;
import com.dev.zine.model.Rooms;
import com.dev.zine.model.User;
import com.dev.zine.model.UserLastSeen;

@Service
public class UserLastSeenService {

    @Autowired
    private UserLastSeenDAO userLastSeenDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RoomsDAO roomsDAO;

    public void updateLastSeen(String userEmail, Long roomId) throws UserNotFound, RoomDoesNotExist{
        User user = userDAO.findByEmailIgnoreCase(userEmail).orElseThrow(() -> new UserNotFound());
        Rooms room = roomsDAO.findById(roomId).orElseThrow(() -> new RoomDoesNotExist());
        UserLastSeen userLastSeen = userLastSeenDAO.findByUserAndRoom(user, room).orElse(new UserLastSeen(user, room));
        userLastSeen.setLastSeen(Timestamp.valueOf(LocalDateTime.now()));;
        userLastSeenDAO.save(userLastSeen);
    }

    public Timestamp getLastSeen(String userEmail, Long roomId) throws UserNotFound, RoomDoesNotExist{
        User user = userDAO.findByEmailIgnoreCase(userEmail).orElseThrow(() -> new UserNotFound());
        Rooms room = roomsDAO.findById(roomId).orElseThrow(() -> new RoomDoesNotExist());
        return userLastSeenDAO.findByUserAndRoom(user, room)
                         .map(UserLastSeen::getLastSeen)
                         .orElse(null);
    }
}
