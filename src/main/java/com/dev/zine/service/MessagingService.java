package com.dev.zine.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.dev.zine.api.model.messages.MessageBody;
import com.dev.zine.dao.MessagesDAO;
import com.dev.zine.dao.RoomsDAO;
import com.dev.zine.dao.UserDAO;
import com.dev.zine.exceptions.RoomDoesNotExist;
import com.dev.zine.model.Message;
import com.dev.zine.model.Rooms;
import com.dev.zine.model.User;

@Service
public class MessagingService {
    private MessagesDAO messagesDAO;
    private RoomsDAO roomsDAO;
    private UserDAO userDAO;
    private FirebaseMessagingService fcm;
 
    public MessagingService(MessagesDAO messagesDAO, FirebaseMessagingService fcm, UserDAO userDAO, RoomsDAO roomsDAO,SimpMessagingTemplate simpMessagingTemplate ) {
        this.messagesDAO = messagesDAO;
        this.fcm = fcm;
        this.userDAO = userDAO;
        this.roomsDAO = roomsDAO;

    }

    public Message sendMessage(MessageBody msg) throws NoSuchElementException {
        Message newMsg = new Message();
        Rooms room = roomsDAO.findById(msg.getRoomId()).orElseThrow();
        User sentFrom = userDAO.findById(msg.getSentFrom()).orElseThrow();
        newMsg.setType(msg.getType());
        newMsg.setContent(msg.getContent());
        newMsg.setContentUrl(msg.getContentUrl());
        newMsg.setReplyTo(messagesDAO.findById(msg.getReplyTo()).orElse(null));
        newMsg.setRoomId(room);
        newMsg.setSentFrom(sentFrom);
        newMsg.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        messagesDAO.save(newMsg);

        // simpMessagingTemplate.convertAndSend("/room/" + msg.getRoomId(),
        //         msg);
        fcm.sendNotificationToTopic("room" + msg.getRoomId(), room.getName(),
                sentFrom.getName() + ": " + msg.getContent(),
                msg.getContentUrl());

        return newMsg;

    }

    public List<Message> getRoomMessages(long roomId) throws RoomDoesNotExist {
        Rooms room = roomsDAO.findById(roomId).orElse(null);
        if (room != null) {
            // throw new RoomDoesNotExist();
            // List<Message> messages=new List();
            List<Message> messages = messagesDAO.findByRoomIdOrderByTimestampAsc(room);
            return messages;
        } else {
            throw new RoomDoesNotExist();
        }
    }
}
