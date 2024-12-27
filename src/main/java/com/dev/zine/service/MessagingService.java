package com.dev.zine.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.dev.zine.api.model.messages.FileBody;
import com.dev.zine.api.model.messages.MessageBody;
import com.dev.zine.api.model.messages.PollBody;
import com.dev.zine.api.model.messages.TextMsgBody;
import com.dev.zine.dao.MediaDAO;
import com.dev.zine.dao.MessagesDAO;
import com.dev.zine.dao.RoomMembersDAO;
import com.dev.zine.dao.RoomsDAO;
import com.dev.zine.dao.UserDAO;
import com.dev.zine.dao.chat.ChatItemDAO;
import com.dev.zine.dao.chat.MessageDAO;
import com.dev.zine.dao.chat.PollDAO;
import com.dev.zine.dao.chat.PollOptionDAO;
import com.dev.zine.exceptions.NotFoundException;
import com.dev.zine.exceptions.RoomDoesNotExist;
import com.dev.zine.exceptions.UserNotFound;
import com.dev.zine.exceptions.UserNotInRoom;
import com.dev.zine.model.Media;
import com.dev.zine.model.Message;
import com.dev.zine.model.Rooms;
import com.dev.zine.model.User;
import com.dev.zine.model.chat.ChatItem;
import com.dev.zine.model.chat.Poll;
import com.dev.zine.model.chat.PollOption;
import com.dev.zine.model.chat.TextMessage;

@Service
public class MessagingService {
    private MessagesDAO messagesDAO;
    private RoomsDAO roomsDAO;
    private UserDAO userDAO;
    private FirebaseMessagingService fcm;
    @Autowired
    private RoomMembersDAO roomMembersDAO;
    @Autowired
    private ChatItemDAO chatItemDAO;
    @Autowired
    private PollDAO pollDAO;
    @Autowired
    private PollOptionDAO optionDAO;
    @Autowired
    private MessageDAO textDAO;
    @Autowired
    private MediaDAO mediaDAO;
 
    public MessagingService(MessagesDAO messagesDAO, FirebaseMessagingService fcm, UserDAO userDAO, RoomsDAO roomsDAO,SimpMessagingTemplate simpMessagingTemplate ) {
        this.messagesDAO = messagesDAO;
        this.fcm = fcm;
        this.userDAO = userDAO;
        this.roomsDAO = roomsDAO;
    }

    public ChatItem sendMessage(MessageBody msg) throws NoSuchElementException, RoomDoesNotExist, UserNotFound, UserNotInRoom {
        Rooms room = roomsDAO.findById(msg.getRoomId()).orElseThrow(() -> new RoomDoesNotExist());
        User sentFrom = userDAO.findById(msg.getSentFrom()).orElseThrow(() -> new UserNotFound(msg.getSentFrom()));
        // if(!roomMembersDAO.existsByUserAndRoom(sentFrom, room) && sentFrom.getType()!="admin") throw new UserNotInRoom(sentFrom.getId(), room.getId());

        
        ChatItem item = new ChatItem();
        item.setType(msg.getType());
        if("text".equals(msg.getType()) && msg.getTextMessage()!=null) {
            TextMessage text = createTextMessage(msg.getTextMessage());
            // System.out.println("\n\nhiiii " + text.getId().toString() + "\n\n\n");
            item.setTextMessage(text);
        } else if("poll".equals(msg.getType()) && msg.getPoll()!=null) {
            Poll poll = createPoll(msg.getPoll());
            item.setPoll(poll);
        } else if("file".equals(msg.getType()) && msg.getFile()!=null) {
            Media file = getFile(msg.getFile());
            item.setFile(file);
        }
        if(msg.getReplyTo()!=null) item.setReplyTo(chatItemDAO.findById(msg.getReplyTo()).orElse(null));
        item.setRoomId(room);
        item.setSentFrom(sentFrom);
        item.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        chatItemDAO.save(item);

        // simpMessagingTemplate.convertAndSend("/room/" + msg.getRoomId(),
        //         msg);
        // fcm.sendNotificationToTopic("room" + msg.getRoomId()+"", room.getName(),
        //         sentFrom.getName() + ": " + msg.getContent(),
        //         msg.getContentUrl());

        return item;

    }

    public Poll createPoll(PollBody body) {
        Poll newPoll = new Poll();
        newPoll.setDescription(body.getDescription());
        newPoll.setTitle(body.getTitle());
        pollDAO.save(newPoll);
        for(String option: body.getPollOptions()) {
            PollOption newOption = new PollOption();
            newOption.setPoll(newPoll);
            newOption.setValue(option);
            optionDAO.save(newOption);
        }
        return newPoll;
    }

    public TextMessage createTextMessage(TextMsgBody body) {
        TextMessage msg = new TextMessage();
        msg.setContent(body.getContent());
        textDAO.save(msg);
        return msg;
    }
    
    public Media getFile(FileBody body) {
        return mediaDAO.findByUrl(body.getUrl()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Media not found"));
    }

    public List<ChatItem> getRoomMessages(Long roomId) throws RoomDoesNotExist {
        Rooms room = roomsDAO.findById(roomId).orElseThrow(RoomDoesNotExist::new);
        List<ChatItem> messages = chatItemDAO.findByRoomIdOrderByTimestampAsc(room);
        return messages;
    }
}
