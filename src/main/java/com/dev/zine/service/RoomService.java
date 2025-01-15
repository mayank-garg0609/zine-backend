package com.dev.zine.service;

import java.io.IOException;
import java.sql.Timestamp;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dev.zine.api.model.images.ImagesUploadRes;
import com.dev.zine.api.model.room.RoomBody;
import com.dev.zine.api.model.room.RoomResBody;
import com.dev.zine.dao.MediaDAO;
import com.dev.zine.dao.RoomsDAO;
import com.dev.zine.dao.UserDAO;
import com.dev.zine.dao.chat.ChatItemDAO;
import com.dev.zine.exceptions.MediaUploadFailed;
import com.dev.zine.exceptions.RoomDoesNotExist;
import com.dev.zine.exceptions.UserNotFound;
import com.dev.zine.model.Media;
import com.dev.zine.model.Rooms;
import com.dev.zine.model.User;
import com.dev.zine.model.chat.ChatItem;
import com.dev.zine.utils.CloudinaryUtil;

import jakarta.transaction.Transactional;

@Service
public class RoomService {
    @Autowired
    private RoomsDAO roomDAO;
    @Autowired
    private UserLastSeenService userLastSeenService;
    @Autowired
    private ChatItemDAO chatItemDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private CloudinaryUtil imgUtil;
    @Autowired
    private MediaDAO mediaDAO;

    public Rooms createRoom(RoomBody room) {
        Rooms newRoom = new Rooms();

        newRoom.setName(room.getName());
        newRoom.setType(room.getType());
        newRoom.setDescription(room.getDescription());
        newRoom.setDpUrl(room.getDpUrl());
        newRoom.setImagePath(room.getImagePath());

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
                if(room.getImagePath() != null) {
                    existingRoom.setImagePath(room.getImagePath());
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
        Timestamp lastMessageTimestamp = chatItemDAO.findFirstByRoomIdAndDeletedFalseOrderByTimestampDesc(announcementRoom).map(ChatItem::getTimestamp).orElse(null);
        Long unreadMessages = chatItemDAO.countUnreadMessages(announcementRoom, lastSeen);
        RoomResBody body = new RoomResBody();
        body.setRoom(announcementRoom);
        body.setLastMessageTimestamp(lastMessageTimestamp);
        body.setUnreadMessages(unreadMessages);
        body.setUserLastSeen(lastSeen);
        return body;
    } 

    public ImagesUploadRes updateRoomDp(MultipartFile img, Long roomId, boolean delete) throws RoomDoesNotExist, MediaUploadFailed{
        Rooms room = roomDAO.findById(roomId).orElseThrow(() -> new RoomDoesNotExist());
        try {
            ImagesUploadRes res = new ImagesUploadRes();
            if(room.getDpUrl() != null) {
                Optional<Media> oldImage = mediaDAO.findByUrl(room.getDpUrl());
                if(oldImage.isPresent()){
                    mediaDAO.delete(oldImage.get());
                    imgUtil.deleteImage(oldImage.get().getPublicId());
                }
            }
            if(delete) {
                room.setDpUrl(null);
                roomDAO.save(room);
                res.setMessage("Room DP successfully deleted!");
                return res;
            } else {
                res = imgUtil.uploadFile(img, "room-dp");
                Media newImage = new Media();
                newImage.setPublicId(res.getPublicId());
                newImage.setUrl(res.getUrl());
                mediaDAO.save(newImage);
    
                room.setDpUrl(res.getUrl());
                roomDAO.save(room);
                return res;
            }

        } catch(IOException e) {
            throw new MediaUploadFailed(e.getMessage());
        }
    }
}
