package com.dev.zine.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import com.dev.zine.api.model.messages.FileBody;
import com.dev.zine.api.model.messages.PollVoteBody;
import com.dev.zine.api.model.messages.TextMsgBody;
import com.dev.zine.api.model.messages.creation.MessageCreateBody;
import com.dev.zine.api.model.messages.creation.PollCreateBody;
import com.dev.zine.api.model.messages.edit.PollOptionEditBody;
import com.dev.zine.api.model.messages.response.BroadcastMsgBody;
import com.dev.zine.api.model.messages.response.MsgReplyBody;
import com.dev.zine.api.model.messages.response.MsgResBody;
import com.dev.zine.api.model.messages.response.PollOptionResBody;
import com.dev.zine.api.model.messages.response.PollResBody;
import com.dev.zine.api.model.messages.response.PollVoteRes;
import com.dev.zine.api.model.messages.response.SentFromBody;
import com.dev.zine.dao.MediaDAO;
import com.dev.zine.dao.RoomMembersDAO;
import com.dev.zine.dao.RoomsDAO;
import com.dev.zine.dao.UserDAO;
import com.dev.zine.dao.chat.ChatItemDAO;
import com.dev.zine.dao.chat.MessageDAO;
import com.dev.zine.dao.chat.PollDAO;
import com.dev.zine.dao.chat.PollOptionDAO;
import com.dev.zine.dao.chat.PollVoteDAO;
import com.dev.zine.exceptions.NotFoundException;
import com.dev.zine.exceptions.RoomDoesNotExist;
import com.dev.zine.exceptions.UserNotFound;
import com.dev.zine.exceptions.UserNotInRoom;
import com.dev.zine.model.Media;
import com.dev.zine.model.Rooms;
import com.dev.zine.model.User;
import com.dev.zine.model.chat.ChatItem;
import com.dev.zine.model.chat.Poll;
import com.dev.zine.model.chat.PollOption;
import com.dev.zine.model.chat.PollVote;
import com.dev.zine.model.chat.TextMessage;

@Service
public class MessagingService {
    private RoomsDAO roomsDAO;
    private UserDAO userDAO;
    private FirebaseMessagingService fcm;
    private SimpMessagingTemplate simpMessagingTemplate;
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
    @Autowired
    private PollVoteDAO pollVoteDAO;

    public MessagingService(FirebaseMessagingService fcm, UserDAO userDAO, RoomsDAO roomsDAO,
            SimpMessagingTemplate simpMessagingTemplate) {
        this.fcm = fcm;
        this.userDAO = userDAO;
        this.roomsDAO = roomsDAO;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Transactional
    public BroadcastMsgBody sendMessage(MessageCreateBody msg)
            throws NoSuchElementException, RoomDoesNotExist, UserNotFound, UserNotInRoom {
        Rooms room = roomsDAO.findById(msg.getRoomId()).orElseThrow(() -> new RoomDoesNotExist());
        User sentFrom = userDAO.findById(msg.getSentFrom()).orElseThrow(() -> new UserNotFound(msg.getSentFrom()));
        if (!roomMembersDAO.existsByUserAndRoom(sentFrom, room) && !"admin".equals(sentFrom.getType()))
            throw new UserNotInRoom(sentFrom.getId(), room.getId());

        ChatItem item = new ChatItem();
        item.setType(msg.getType());
        if ("text".equals(msg.getType()) && msg.getText() != null) {
            TextMessage text = createTextMessage(msg.getText());
            item.setTextMessage(text);
        } else if ("poll".equals(msg.getType()) && msg.getPoll() != null) {
            Poll poll = createPoll(msg.getPoll());
            item.setPoll(poll);
        } else if ("file".equals(msg.getType()) && msg.getFile() != null) {
            Media file = getFile(msg.getFile());
            item.setFile(file);
        }
        if (msg.getReplyTo() != null)
            item.setReplyTo(chatItemDAO.findById(msg.getReplyTo()).orElse(null));
        item.setRoomId(room);
        item.setSentFrom(sentFrom);
        item.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        // System.out.println("hii1111");
        chatItemDAO.save(item);
        // System.out.println("hii1112");

        // simpMessagingTemplate.convertAndSend("/room/" + msg.getRoomId(),
        // msg);
        fcmMessageBuilder(item);

        return constructBroadcastMsg("new-message", item);

    }

    public void fcmMessageBuilder(ChatItem item) {
        Map<String, String> bodyArgs = new HashMap<>();
        bodyArgs.put("roomId", item.getRoomId().getId().toString());
        bodyArgs.put("chatItemId", item.getId().toString());
        bodyArgs.put("userEmail", item.getSentFrom().getEmail());

        if ("text".equals(item.getType())) {
            fcm.sendNotificationToTopic("room" + item.getRoomId().getId() + "", item.getRoomId().getName(),
                    item.getSentFrom().getName() + ": " + item.getTextMessage().getContent(), "", bodyArgs);
        } else if ("poll".equals(item.getType())) {
            fcm.sendNotificationToTopic("room" + item.getRoomId().getId() + "", item.getRoomId().getName(),
                    item.getSentFrom().getName() + " shared a poll: "+item.getPoll().getTitle(), "", bodyArgs);
        } else if("file".equals(item.getType())) {
            fcm.sendNotificationToTopic("room" + item.getRoomId().getId() + "", item.getRoomId().getName(),
                    item.getSentFrom().getName() + " shared a file: "+item.getFile().getName(), "", bodyArgs);
        }
    }

    public Poll createPoll(PollCreateBody body) {
        Poll newPoll = new Poll();
        newPoll.setDescription(body.getDescription());
        newPoll.setTitle(body.getTitle());
        pollDAO.save(newPoll);
        for (String option : body.getPollOptions()) {
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
        return mediaDAO.findByUrl(body.getUrl())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Media not found"));
    }

    public List<MsgResBody> getRoomMessages(Long roomId, User user) throws RoomDoesNotExist {
        Rooms room = roomsDAO.findById(roomId).orElseThrow(RoomDoesNotExist::new);
        List<ChatItem> messages = chatItemDAO.findByRoomIdOrderByTimestampAsc(room);
        return messages.stream().map(item -> {
            MsgResBody body = new MsgResBody();
            body.setId(item.getId());
            body.setType(item.getType());
            body.setDeleted(item.isDeleted());
            if ("text".equals(item.getType())) {
                TextMsgBody textBody = new TextMsgBody();
                textBody.setContent(item.getTextMessage().getContent());
                body.setText(textBody);
            } else if ("file".equals(item.getType())) {
                FileBody fileBody = new FileBody();
                fileBody.setDescription(item.getFile().getDescription());
                fileBody.setName(item.getFile().getName());
                fileBody.setUrl(item.getFile().getUrl());
                body.setFile(fileBody);
            } else if ("poll".equals(item.getType())) {
                PollResBody pollResBody = new PollResBody();
                pollResBody.setDescription(item.getPoll().getDescription());
                pollResBody.setTitle(item.getPoll().getTitle());
                pollResBody.setOptions(item.getPoll().getPollOptions().stream().map(option -> {
                    PollOptionResBody res = new PollOptionResBody();
                    res.setValue(option.getValue());
                    res.setId(option.getId());
                    res.setNumVotes(option.getPollVotes().size());
                    return res;
                })
                        .collect(Collectors.toList()));
                PollVote lastVote = pollVoteDAO.findByVoterAndPoll(user, item.getPoll()).orElse(null);
                if (lastVote != null)
                    pollResBody.setLastVoted(lastVote.getOption().getId());
                body.setPoll(pollResBody);
            }
            SentFromBody sentFromBody = new SentFromBody();
            sentFromBody.setId(item.getSentFrom().getId());
            sentFromBody.setName(item.getSentFrom().getName());
            body.setSentFrom(sentFromBody);
            body.setTimestamp(item.getTimestamp());
            if (item.getReplyTo() != null) {
                MsgReplyBody msgReplyBody = new MsgReplyBody();
                msgReplyBody.setId(item.getReplyTo().getId());
                body.setReplyTo(msgReplyBody);
            }
            return body;
        }).collect(Collectors.toList());
    }

    @Transactional
    public BroadcastMsgBody constructBroadcastMsg(String update, ChatItem item) {
        BroadcastMsgBody res = new BroadcastMsgBody();
        MsgResBody body = new MsgResBody();
        body.setId(item.getId());
        body.setType(item.getType());
        body.setDeleted(item.isDeleted());
        if (item.isDeleted())
            System.out.println("msg " + item.getId().toString());
        else
            System.out.println("not deleted " + item.getId().toString());

        if ("text".equals(item.getType())) {
            TextMsgBody textBody = new TextMsgBody();
            textBody.setContent(item.getTextMessage().getContent());
            body.setText(textBody);
        } else if ("file".equals(item.getType())) {
            FileBody fileBody = new FileBody();
            fileBody.setDescription(item.getFile().getDescription());
            fileBody.setName(item.getFile().getName());
            fileBody.setUrl(item.getFile().getUrl());
            body.setFile(fileBody);
        } else if ("poll".equals(item.getType())) {
            PollResBody pollResBody = new PollResBody();
            pollResBody.setDescription(item.getPoll().getDescription());
            pollResBody.setTitle(item.getPoll().getTitle());
            List<PollOption> op = optionDAO.findAllByPoll(item.getPoll());
            pollResBody.setOptions(op.stream()
                    .map(option -> {
                        PollOptionResBody optionRes = new PollOptionResBody();
                        optionRes.setValue(option.getValue());
                        optionRes.setId(option.getId());
                        optionRes.setNumVotes(option.getPollVotes().size());
                        return optionRes;
                    })
                    .collect(Collectors.toList()));
            body.setPoll(pollResBody);
        }
        SentFromBody sentFromBody = new SentFromBody();
        sentFromBody.setId(item.getSentFrom().getId());
        sentFromBody.setName(item.getSentFrom().getName());
        body.setSentFrom(sentFromBody);
        body.setTimestamp(item.getTimestamp());
        if (item.getReplyTo() != null) {
            MsgReplyBody msgReplyBody = new MsgReplyBody();
            msgReplyBody.setId(item.getReplyTo().getId());
            body.setReplyTo(msgReplyBody);
        }
        res.setUpdate(update);
        res.setBody(body);
        return res;
    }

    @Transactional
    public void registerVote(PollVoteBody vote) throws UserNotFound, NotFoundException {
        User voter = userDAO.findById(vote.getVoterId()).orElseThrow(() -> new UserNotFound(vote.getVoterId()));
        ChatItem chatItem = chatItemDAO.findById(vote.getChatId())
                .orElseThrow(() -> new NotFoundException("ChatItem", vote.getChatId()));
        if (chatItem.getPoll() == null)
            return;
        PollOption pollOption = optionDAO.findById(vote.getOptionId())
                .orElseThrow(() -> new NotFoundException("PollOption", vote.getChatId()));
        pollVoteDAO.deleteAllByVoterAndPoll(voter, chatItem.getPoll());
        PollVote newVote = new PollVote();
        newVote.setOption(pollOption);
        newVote.setPoll(chatItem.getPoll());
        newVote.setVoter(voter);
        pollVoteDAO.save(newVote);

        BroadcastMsgBody res = new BroadcastMsgBody();

        PollVoteRes voteRes = new PollVoteRes();
        voteRes.setChatItemId(chatItem.getId());
        voteRes.setPollOptions(chatItem.getPoll().getPollOptions().stream().map(option -> {
            PollOptionResBody pollRes = new PollOptionResBody();
            pollRes.setValue(option.getValue());
            pollRes.setId(option.getId());
            pollRes.setNumVotes(pollVoteDAO.countByOption(option));
            return pollRes;
        }).collect(Collectors.toList()));

        res.setPollUpdate(voteRes);
        res.setUpdate("poll-update");

        simpMessagingTemplate.convertAndSend("/room/" + chatItem.getRoomId().getId(),
                res);
    }

    public void editMessage(Long id, MessageCreateBody msg, User user) throws NotFoundException {
        ChatItem item = chatItemDAO.findById(id).orElseThrow(() -> new NotFoundException("ChatItem", id));
        if ("admin".equals(user.getType()) || item.getSentFrom() == user) {
            if ("text".equals(msg.getType())) {
                if (msg.getText().getClass() != null)
                    item.getTextMessage().setContent(msg.getText().getContent());
                textDAO.save(item.getTextMessage());
            } else if ("poll".equals(msg.getType())) {
                if (msg.getPollEditBody().getDescription() != null)
                    item.getPoll().setDescription(msg.getPollEditBody().getDescription());
                if (msg.getPollEditBody().getTitle() != null)
                    item.getPoll().setTitle(msg.getPollEditBody().getTitle());
                for (PollOptionEditBody body : msg.getPollEditBody().getOptions()) {
                    if ("update".equals(body.getAction())) {
                        PollOption option = optionDAO.findById(body.getOptionId())
                                .orElseThrow(() -> new NotFoundException("PollOption", body.getOptionId()));
                        option.setValue(body.getValue());
                        optionDAO.save(option);
                    } else if ("delete".equals(body.getAction())) {
                        PollOption option = optionDAO.findById(body.getOptionId())
                                .orElseThrow(() -> new NotFoundException("PollOption", body.getOptionId()));
                        optionDAO.delete(option);
                    } else if ("add".equals(body.getAction())) {
                        PollOption newOption = new PollOption();
                        newOption.setPoll(item.getPoll());
                        newOption.setValue(body.getValue());
                        optionDAO.save(newOption);
                    }
                }
            }
        }

    }

    public void deleteMessage(Long id, User user) throws NotFoundException {
        ChatItem item = chatItemDAO.findById(id).orElseThrow(() -> new NotFoundException("ChatItem", id));

        if ("admin".equals(user.getType()) || user.equals(item.getSentFrom())) {
            item.setDeleted(true);
            chatItemDAO.findByReplyTo(item).forEach(replied -> {
                replied.setReplyTo(null);
                chatItemDAO.save(replied);
            });
            chatItemDAO.save(item);
        }
    }

}
