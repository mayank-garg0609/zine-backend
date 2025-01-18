package com.dev.zine.service;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.sql.exec.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.dev.zine.api.model.roomMembers.MembersList;
import com.dev.zine.api.model.roomMembers.MembersResponse;
import com.dev.zine.api.model.roomMembers.MembersRoomsList;
import com.dev.zine.api.model.roomMembers.RemoveMembersList;
import com.dev.zine.api.model.roomMembers.RemoveMembersRoomsList;
import com.dev.zine.api.model.user.AssignResponse;
import com.dev.zine.api.model.user.AssignResponseMultipleRooms;
import com.dev.zine.api.model.room.RoomResBody;
import com.dev.zine.api.model.roomMembers.MemberRoleUpdate;
import com.dev.zine.api.model.roomMembers.Members;
import com.dev.zine.dao.RoomMembersDAO;
import com.dev.zine.dao.RoomsDAO;
import com.dev.zine.dao.UserDAO;
import com.dev.zine.dao.chat.ChatItemDAO;
import com.dev.zine.exceptions.RoomDoesNotExist;
import com.dev.zine.exceptions.RoomMemberNotFound;
import com.dev.zine.model.RoomMembers;
import com.dev.zine.model.Rooms;
import com.dev.zine.model.User;
import com.dev.zine.model.chat.ChatItem;

import org.springframework.http.HttpStatus;
import jakarta.transaction.Transactional;

@Service
public class RoomMembersService {
    @Autowired
    private RoomsDAO roomDAO;
    @Autowired
    private RoomMembersDAO roomMembersDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RoomService roomService;
    @Autowired
    private FirebaseMessagingService firebaseMessagingService;
    @Autowired
    private UserLastSeenService userLastSeenService;
    @Autowired
    private ChatItemDAO chatItemDAO;

    public ResponseEntity<List<RoomResBody>> getRoomsByEmail(String email) {
        try {
            Optional<User> opUser = userDAO.findByEmailIgnoreCase(email);

            if (opUser.isPresent()) {
                User user = opUser.get();
                List<RoomResBody> roomList = new ArrayList<>();

                List<RoomMembers> l = roomMembersDAO.findByUser(user);
                for (RoomMembers rm : l) {
                    Optional<Rooms> opRoom = roomService.getRoomInfo(rm.getRoom().getId());
                    if (opRoom.isPresent()) {
                        Rooms room = opRoom.get();
                        Timestamp lastSeen = userLastSeenService.getLastSeen(user, room);
                        Timestamp lastMessageTimestamp = chatItemDAO
                                .findFirstByRoomIdAndDeletedFalseOrderByTimestampDesc(room).map(ChatItem::getTimestamp)
                                .orElse(null);
                        Long unreadMessages = chatItemDAO.countUnreadMessages(room, lastSeen);
                        RoomResBody body = new RoomResBody();
                        body.setRoom(room);
                        body.setLastMessageTimestamp(lastMessageTimestamp);
                        body.setUnreadMessages(unreadMessages);
                        body.setUserLastSeen(lastSeen);
                        roomList.add(body);
                    }
                }
                return ResponseEntity.ok().body(roomList);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public AssignResponse addMembers(MembersList addMembersBody)
            throws RoomDoesNotExist, InterruptedException, ExecutionException {
        try {
            Long roomId = addMembersBody.getRoom();
            List<Members> members = addMembersBody.getMembers();

            List<String> invalidEmails = new ArrayList<>();
            List<String> alreadyAssignedUser = new ArrayList<>();

            Rooms room = roomDAO.findById(roomId).orElseThrow(RoomDoesNotExist::new);

            List<RoomMembers> roomMembers = new ArrayList<>();
            for (Members memberPair : members) {
                String memberId = memberPair.getUserEmail();
                String role = memberPair.getRole();
                User member = userDAO.findByEmailIgnoreCase(memberId).orElse(null);

                if (member != null && !roomMembersDAO.existsByUserAndRoom(member, room)) {
                    RoomMembers roomMember = new RoomMembers();
                    roomMember.setRoom(room);
                    roomMember.setUser(member);
                    roomMember.setRole(role);
                    roomMembers.add(roomMember);
                } else if (member != null) {
                    alreadyAssignedUser.add(memberId);
                } else {
                    invalidEmails.add(memberId);
                }
            }
            if (invalidEmails.isEmpty() && alreadyAssignedUser.isEmpty()) {
                roomMembersDAO.saveAll(roomMembers);
                List<String> tokens = roomMembers.stream()
                        .map(roomMember -> roomMember.getUser().getPushToken()) 
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()); 
                if(!tokens.isEmpty()) {
                    firebaseMessagingService.subscribeToTopic(tokens, "room" + room.getId());
                }

                return new AssignResponse("success");
            }
            return new AssignResponse("fail", invalidEmails, alreadyAssignedUser);
        } catch (Exception e) {
            e.printStackTrace();
            return new AssignResponse("fail");
        }
    }

    public AssignResponseMultipleRooms addMembersToRooms(MembersRoomsList body) throws RoomDoesNotExist {
        try {
            List<Long> roomIds = body.getRoomIds();
            List<Members> members = body.getMembers();

            Set<String> invalidEmails = new HashSet<>();
            Map<Long, List<String>> alreadyAssignedUsers = new HashMap<>();
            List<RoomMembers> allRoomMembers = new ArrayList<>();

            for (Long roomId : roomIds) {
                Rooms room = roomDAO.findById(roomId).orElseThrow(RoomDoesNotExist::new);

                for (Members memberPair : members) {
                    String memberId = memberPair.getUserEmail();
                    String role = memberPair.getRole();
                    User member = userDAO.findByEmailIgnoreCase(memberId).orElse(null);

                    if (member != null) {
                        if (!roomMembersDAO.existsByUserAndRoom(member, room)) {
                            RoomMembers roomMember = new RoomMembers();
                            roomMember.setRoom(room);
                            roomMember.setUser(member);
                            roomMember.setRole(role);
                            allRoomMembers.add(roomMember);
                        } else {
                            alreadyAssignedUsers.computeIfAbsent(roomId, k -> new ArrayList<>()).add(memberId);
                        }
                    } else {
                        invalidEmails.add(memberId);
                    }
                }
            }

            if (!allRoomMembers.isEmpty()) {
                roomMembersDAO.saveAll(allRoomMembers);

                Map<Long, List<String>> roomTokensMap = allRoomMembers.stream()
                        .collect(Collectors.groupingBy(
                                rm -> rm.getRoom().getId(),
                                Collectors.mapping(
                                        rm -> rm.getUser().getPushToken(),
                                        Collectors.filtering(Objects::nonNull, Collectors.toList()))));

                for (Map.Entry<Long, List<String>> entry : roomTokensMap.entrySet()) {
                    if(!entry.getValue().isEmpty()) {
                        firebaseMessagingService.subscribeToTopic(entry.getValue(), "room" + entry.getKey());
                    }
                }
            }

            if (invalidEmails.isEmpty() && alreadyAssignedUsers.isEmpty()) {
                return new AssignResponseMultipleRooms("success");
            }

            return new AssignResponseMultipleRooms("partial_success", invalidEmails, alreadyAssignedUsers);

        } catch (Exception e) {
            e.printStackTrace();
            return new AssignResponseMultipleRooms("fail");
        }
    }

    @Transactional
    public void removeMembersFromRooms(RemoveMembersRoomsList body) throws RoomDoesNotExist {
        try {
            List<User> users = userDAO.findByEmailIn(body.getMembers());
            List<String> tokens = new ArrayList<>();
            for (User user : users) {
                if (user.getPushToken() != null) {
                    tokens.add(user.getPushToken());
                }
            }

            for (Long roomId : body.getRoomIds()) {
                Rooms room = roomDAO.findById(roomId).orElse(null);
                if (room != null) {
                    for (User member : users) {
                        roomMembersDAO.deleteByUserAndRoom(member, room);
                    }
                    if (!tokens.isEmpty()) {
                        firebaseMessagingService.unsubscribeFromTopic(tokens, "room" + roomId.toString());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Transactional
    public String removeMembers(RemoveMembersList removeMembersList) throws RoomDoesNotExist {
        Long roomId = removeMembersList.getRoom();
        Rooms room = roomDAO.findById(roomId).orElse(null);
        List<User> users = userDAO.findByEmailIn(removeMembersList.getMembers());
        List<String> tokens = new ArrayList<>();
        for (User user : users) {
            if (user.getPushToken() != null) {
                tokens.add(user.getPushToken());
            }
        }
        System.out.println(users);

        if (room != null) {
            for (User user : users) {
                if (user != null) {
                    roomMembersDAO.deleteByUserAndRoom(user, room);
                }
            }
            if (!tokens.isEmpty()) {
                firebaseMessagingService.unsubscribeFromTopic(tokens, "room" + roomId.toString());
            }

            return "All users remove From room " + room.getName();

        } else {

            throw new RoomDoesNotExist();

        }

    }

    public void updateMemberRole(MemberRoleUpdate update) throws RoomDoesNotExist, RoomMemberNotFound {
        try {
            Rooms room = roomDAO.findById(update.getRoom()).orElse(null);
            User user = userDAO.findByEmailIgnoreCase(update.getMemberEmail()).orElse(null);

            if (room != null && user != null) {
                RoomMembers existing = roomMembersDAO.findByRoomAndUser(room, user).orElse(null);
                if (existing != null) {
                    existing.setRole(update.getRole());
                    roomMembersDAO.save(existing);
                } else {
                    throw new RoomMemberNotFound();
                }
            } else {
                throw new RoomDoesNotExist();
            }
        } catch (RoomMemberNotFound e) {
            throw e;
        } catch (RoomDoesNotExist e) {
            throw e;
        }
    }

    public List<MembersResponse> getMemebers(Long roomId) throws RoomDoesNotExist {
        // List<MembersResponse> res = new ArrayList<>();
        Rooms room = roomDAO.findById(roomId).orElse(null);
        if (room != null) {
            List<RoomMembers> roomMembers = roomMembersDAO.findByRoom(room);

            return roomMembers.stream()
                    .map(roomMember -> {
                        User user = roomMember.getUser();
                        MembersResponse response = new MembersResponse();
                        response.setName(user.getName());
                        response.setEmail(user.getEmail());
                        response.setRole(roomMember.getRole());
                        response.setDpUrl(user.getDp());
                        response.setId(user.getId());
                        return response;
                    })
                    .collect(Collectors.toList());

        } else {
            throw new RoomDoesNotExist();
        }

        // return res;
    }

}
