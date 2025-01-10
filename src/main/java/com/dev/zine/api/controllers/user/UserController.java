package com.dev.zine.api.controllers.user;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.sql.exec.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dev.zine.api.model.images.ImagesUploadRes;
import com.dev.zine.api.model.task.TaskInstanceCreateBody;
import com.dev.zine.api.model.task.UserTasksBody;
import com.dev.zine.api.model.user.DeletionRequest;
import com.dev.zine.api.model.user.RoomLastSeenInfo;
import com.dev.zine.api.model.user.TokenUpdateBody;
import com.dev.zine.dao.RoomsDAO;
import com.dev.zine.dao.UserDAO;
import com.dev.zine.exceptions.IncorrectPasswordException;
import com.dev.zine.exceptions.MediaUploadFailed;
import com.dev.zine.exceptions.RoomDoesNotExist;
import com.dev.zine.exceptions.TaskNotFoundException;
import com.dev.zine.exceptions.UserNotFound;
import com.dev.zine.model.Rooms;
import com.dev.zine.model.User;
import com.dev.zine.service.EncryptionService;
import com.dev.zine.service.FirebaseMessagingService;
import com.dev.zine.service.TaskService;
import com.dev.zine.service.UserLastSeenService;
import com.dev.zine.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserLastSeenService lastSeenService;
    @Autowired
    private RoomsDAO roomsDAO;
    @Autowired
    private FirebaseMessagingService firebaseMessagingService;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private EncryptionService encryptionService;
    @Autowired
    private TaskService taskService;

    @PutMapping("/token")
    public ResponseEntity<?> fcmUpdate(@RequestBody TokenUpdateBody body) throws InterruptedException, ExecutionException {
        try{
            userService.updateTokenHelper(body);
            List<Rooms> rooms = roomsDAO.findByType("announcement");
            Rooms announcementRoom = rooms.get(0);
            List<String> tokens = new ArrayList<>();
            tokens.add(body.getToken());
            firebaseMessagingService.subscribeToTopic(tokens, "room"+announcementRoom.getId());
            return ResponseEntity.ok().build();
        } catch(UserNotFound e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{userEmail}/{roomId}/seen")
    public ResponseEntity<?> updateLastSeen(@PathVariable String userEmail, @PathVariable Long roomId) {
        try {
            lastSeenService.updateLastSeen(userEmail, roomId);
            return ResponseEntity.ok().build();
        } catch(UserNotFound | RoomDoesNotExist e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{userEmail}/{roomId}/last-seen")
    public ResponseEntity<?> getLastSeen(@PathVariable String userEmail, @PathVariable Long roomId) {
        try {
            RoomLastSeenInfo lastSeen = lastSeenService.getRoomLastSeenInfo(userEmail, roomId);
            return ResponseEntity.ok().body(Map.of("info", lastSeen));
        } catch(UserNotFound | RoomDoesNotExist e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    

    @PostMapping("/delete")
    public ResponseEntity<?> deleteUserAccount(@RequestBody DeletionRequest deletionRequest) throws UserNotFound, IncorrectPasswordException {
        try {
            User user = userDAO.findByEmailIgnoreCase(deletionRequest.getEmail()).orElseThrow(UserNotFound::new); 
            
            if (encryptionService.verifyPassword(deletionRequest.getPassword(), user.getPassword())) { 
                userService.sendDeletionEmail(deletionRequest.getEmail(), deletionRequest.getDeletionOption());
                return ResponseEntity.ok().body(null);
            } else {
                throw new IncorrectPasswordException();
            }
        } catch(UserNotFound| IncorrectPasswordException e) {
            return ResponseEntity.badRequest().build();
        }
        
    }

    @PostMapping("/tasks/{taskId}/instance") 
    public ResponseEntity<?> chooseTask(@PathVariable Long taskId, @AuthenticationPrincipal User user, @RequestBody TaskInstanceCreateBody body) {
        try {
            if(user==null) throw new UserNotFound();
            UserTasksBody resBody = taskService.chooseTaskByUser(taskId, user, body);
            return ResponseEntity.ok().body(Map.of("instance", resBody));
        } catch(UserNotFound | TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/register/workshop")
    public ResponseEntity<?> registerWorkshop(@AuthenticationPrincipal User user) {
        try {
            String res = userService.toggleRegistration(user);
            if(res=="ALREADY_REGISTERED") {
                return ResponseEntity.badRequest().body(Map.of("message","User is already registered"));
            } else if(res=="NOT_EMAIL_VERIFIED") {
                return ResponseEntity.badRequest().body(Map.of("message","User is not email verified"));
            } else if(res=="SUCCESS") {
                return ResponseEntity.ok().body(Map.of("message","success"));
            } else if(res=="TOKEN_MISSING") {
                return ResponseEntity.badRequest().body(Map.of("message","JWT token is missing"));
            } else
                return ResponseEntity.internalServerError().body(Map.of("message","failed"));
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message","failed"));
        }
    }

    @PostMapping("/update-dp")
    public ResponseEntity<?> uploadUserDp(@AuthenticationPrincipal User user, @RequestParam MultipartFile file, @RequestParam boolean delete) {
        try {
            ImagesUploadRes res = userService.updateDp(user, file, delete);
            return ResponseEntity.ok().body(res);
        } catch(UserNotFound | MediaUploadFailed e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

}

