package com.dev.zine.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.dev.zine.api.model.roomMembers.MembersList;
import com.dev.zine.api.model.roomMembers.MembersResponse;
import com.dev.zine.api.model.roomMembers.RemoveMembersList;
import com.dev.zine.api.model.roomMembers.MemberRoleUpdate;
import com.dev.zine.api.model.roomMembers.Members;
import com.dev.zine.dao.RoomMembersDAO;
import com.dev.zine.dao.RoomsDAO;
import com.dev.zine.dao.UserDAO;
import com.dev.zine.exceptions.RoomDoesNotExist;
import com.dev.zine.exceptions.RoomMemberNotFound;
import com.dev.zine.model.RoomMembers;
import com.dev.zine.model.Rooms;
import com.dev.zine.model.User;

import org.springframework.http.HttpStatus;
import jakarta.transaction.Transactional;

@Service
public class RoomMembersService {

    private RoomsDAO roomDAO;
    private RoomMembersDAO roomMembersDAO;
    private UserDAO userDAO;
    private RoomService roomService;

    @Autowired
    public RoomMembersService(RoomsDAO roomsDAO, RoomMembersDAO roomMembersDAO, UserDAO userDAO, RoomService roomService) {
        this.roomDAO = roomsDAO;
        this.roomMembersDAO = roomMembersDAO;
        this.userDAO = userDAO;
        this.roomService = roomService;
    }
    public ResponseEntity<List<Rooms>> getRoomsByEmail(String email) {
        try{
            Optional<User> opUser = userDAO.findByEmailIgnoreCase(email);
            if(opUser.isPresent()){
                User user = opUser.get();
                List<RoomMembers> l = roomMembersDAO.findByUser(user);
                List<Rooms> resRooms = new ArrayList<>();
                for(RoomMembers rm: l){
                    Optional<Rooms> opRoom = roomService.getRoomInfo(rm.getRoom().getId());
                    if(opRoom.isPresent()){
                        Rooms room = opRoom.get();
                        resRooms.add(room);
                    }
                }
                return ResponseEntity.ok().body(resRooms);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }
        catch(Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public String addMembers(MembersList addMembersBody) throws RoomDoesNotExist {

        Long roomId = addMembersBody.getRoom();
        List<Members> members = addMembersBody.getMembers();
        String message = "";
        Rooms room = roomDAO.findById(roomId).orElse(null);

        if (room != null) {
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
                } else {
                    if (member == null)
                        message += memberId.toString() + " ";
                }

            }
            roomMembersDAO.saveAll(roomMembers);
            if (message.equals("")) {
                return "All users added to room " + room.getName();
            } else {
                return message;
            }
        } else {

            throw new RoomDoesNotExist();

        }

    }

    @Transactional
    public String removeMembers(RemoveMembersList removeMembersList) throws RoomDoesNotExist {
        Long roomId = removeMembersList.getRoom();
        Rooms room = roomDAO.findById(roomId).orElse(null);
        List<User> users = userDAO.findByEmailIn(removeMembersList.getMembers());
        System.out.println(users);

        if (room != null) {
            for (User user : users) {
                if (user != null) {
                    roomMembersDAO.deleteByUserAndRoom(user, room);
                }
            }

            return "All users remove From room " + room.getName();

        } else {

            throw new RoomDoesNotExist();

        }

    }

    public void updateMemberRole(MemberRoleUpdate update) throws RoomDoesNotExist, RoomMemberNotFound{
        try{
            Rooms room = roomDAO.findById(update.getRoom()).orElse(null);
            User user = userDAO.findByEmailIgnoreCase(update.getMemberEmail()).orElse(null);
    
            if(room != null && user != null){
                RoomMembers existing = roomMembersDAO.findByRoomAndUser(room, user).orElse(null);
                if(existing != null){
                    existing.setRole(update.getRole());
                    roomMembersDAO.save(existing);
                } else{
                    throw new RoomMemberNotFound();
                }
            } else{
                throw new RoomDoesNotExist();
            }
        } catch(RoomMemberNotFound e){
            throw e;
        } catch(RoomDoesNotExist e){
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
                        return response;
                    })
                    .collect(Collectors.toList());

        } else {
            throw new RoomDoesNotExist();
        }

        // return res;
    }

}
