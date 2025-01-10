package com.dev.zine.service;

import org.hibernate.sql.exec.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.dev.zine.api.model.auth.LoginBody;
import com.dev.zine.api.model.auth.PasswordResetBody;
import com.dev.zine.api.model.auth.RegistrationBody;
import com.dev.zine.api.model.images.ImagesUploadRes;
import com.dev.zine.api.model.roomMembers.Members;
import com.dev.zine.api.model.roomMembers.MembersList;
import com.dev.zine.api.model.user.TokenUpdateBody;
import com.dev.zine.dao.MediaDAO;
import com.dev.zine.dao.RoleDAO;
import com.dev.zine.dao.RoomMembersDAO;
import com.dev.zine.dao.RoomsDAO;
import com.dev.zine.dao.UserDAO;
import com.dev.zine.dao.UserToRoleDAO;
import com.dev.zine.dao.VerificationTokenDAO;
import com.dev.zine.exceptions.EmailFailureException;
import com.dev.zine.exceptions.EmailNotFoundException;
import com.dev.zine.exceptions.IncorrectPasswordException;
import com.dev.zine.exceptions.MediaUploadFailed;
import com.dev.zine.exceptions.RoomDoesNotExist;
import com.dev.zine.exceptions.UserAlreadyExistsException;
import com.dev.zine.exceptions.UserNotFound;
import com.dev.zine.exceptions.UserNotVerifiedException;
import com.dev.zine.model.Media;
import com.dev.zine.model.Role;
import com.dev.zine.model.RoomMembers;
import com.dev.zine.model.User;
import com.dev.zine.model.UserToRole;
import com.dev.zine.model.VerificationToken;
import com.dev.zine.utils.CloudinaryUtil;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private VerificationTokenDAO verificationTokenDAO;
    @Autowired
    private EncryptionService encryptionService;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private UserToRoleDAO userToRoleDAO;
    @Autowired
    private RoomMembersDAO roomMembersDAO;
    @Autowired FirebaseMessagingService firebaseMessagingService;
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private MediaDAO mediaDAO;
    @Autowired
    private RoomsDAO roomsDAO;
    @Autowired
    private RoomMembersService roomMembersService;
    @Autowired
    private CloudinaryUtil mediaUtil;
    private String regex2024 = "^2024.*@mnit\\.ac\\.in$";
 
    /**
     * Attempts to register a user given the information provided.
     * 
     * @param registrationBody The registration information.
     * @return The local user that has been written to the database.
     * @throws UserAlreadyExistsException Thrown if there is already a user with the
     *                                    given information.
     */
    public User registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException {
        if (userDAO.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        User user = new User();
        user.setEmail(registrationBody.getEmail());
        user.setName(registrationBody.getName());
        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));

        VerificationToken verificationToken = createVerificationToken(user);
        try {
            emailService.sendVerificationEmail(verificationToken);
        } catch (EmailFailureException e) {
            e.printStackTrace();
        }

        userDAO.save(user);
        if(user.getEmail().matches(regex2024)) {
            addUserToWorkshopRooms(user);
        }

        // String findRole = user.getEmail().substring(0, 4);
        // Role role = roleDAO.findByRoleName(findRole).orElse(null);
        // if(role != null) {
        //     UserToRole roleMapping = new UserToRole();
        //     roleMapping.setRole(role);
        //     roleMapping.setUser(user);
        //     userToRoleDAO.save(roleMapping);
        // }

        return user; 
    }

    /**
     * Creates a VerificationToken object for sending to the user.
     * 
     * @param user The user the token is being generated for.
     * @return The object created.
     */
    private VerificationToken createVerificationToken(User user) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(jwtService.generateVerificationJWT(user));
        verificationToken.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
        verificationToken.setUser(user);

        user.getVerificationTokens().add(verificationToken); //adds token to the list
        return verificationToken;
    }

    public void sendDeletionEmail(String email, String deletionOption) {
        String applicationNumber = UUID.randomUUID().toString();
        String messageText = String.format(
                "<html><body>" +
                "<p>Dear user,</p>" +
                "<p>Your account and associated data will be <strong>%s</strong> deleted in 30 days. If you wish to cancel this request, please contact support.</p>" +
                "<p>Your application number is: <strong>%s</strong></p>" +
                "<p>Thank you.</p>" +
                "</body></html>",
                deletionOption.equals("full") ? "fully" : "partially",
                applicationNumber
        );

        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("Account Deletion Notice");
            helper.setText(messageText, true); // true indicates HTML content

            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public String loginUser(LoginBody loginBody) throws UserNotVerifiedException, EmailFailureException, UserNotFound, IncorrectPasswordException {
        Optional<User> opUser = userDAO.findByEmailIgnoreCase(loginBody.getEmail()); // checks if user exists
        if (opUser.isPresent()) {
            User user = opUser.get();
            if (encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())) { //verify password
                if (user.isEmailVerified()) {
                    // System.out.println(loginBody.getPushToken());
                    if(user.getEmail().matches(regex2024)) {
                        Role role2024 = roleDAO.findByRoleName("2024").orElse(null);
                        if(role2024 != null && !userToRoleDAO.existsByUserAndRole(user, role2024)) {
                            UserToRole newMapping = new UserToRole();
                            newMapping.setRole(role2024);
                            newMapping.setUser(user);
                            userToRoleDAO.save(newMapping);
                        }
                    }
                    if(loginBody.getPushToken() != null) {
                        updateToken(user, loginBody.getPushToken());
                    }
                    return jwtService.generateJWT(user); // generate 
                } else {
                    List<VerificationToken> verificationTokens = user.getVerificationTokens();
                    boolean resend = verificationTokens.isEmpty() || 
                            verificationTokens.get(0).getCreatedTimestamp()
                                    .before(new Timestamp(System.currentTimeMillis() - (60 * 60 * 1000))); 
                                    // checks if tokens list is empty 
                                    //or the latest token is older than 1hr

                    if (resend) {
                        VerificationToken verificationToken = createVerificationToken(user);
                        verificationTokenDAO.save(verificationToken);
                        emailService.sendVerificationEmail(verificationToken);
                    }
                    throw new UserNotVerifiedException(resend);
                }
            } else {
                throw new IncorrectPasswordException();
            }
        } 
        throw new UserNotFound();
    }

    @Transactional
    public boolean verifyUser(String token) {
        System.out.println(token);
        Optional<VerificationToken> opToken = verificationTokenDAO.findByToken(token);
        System.out.println(opToken);
        if (opToken.isPresent()) {
            VerificationToken verificationToken = opToken.get();
            User user = verificationToken.getUser();
            if (!user.isEmailVerified()) {
                user.setEmailVerified(true);
                userDAO.save(user);
                verificationTokenDAO.deleteByUser(user);
                return true;
            }
        }
        return false;
    }

    public void forgotPassword(String email) throws EmailNotFoundException, EmailFailureException {
        Optional<User> opUser = userDAO.findByEmailIgnoreCase(email);
        if (opUser.isPresent()) {
            User user = opUser.get();
            String token = jwtService.generatePasswordResetJWT(user);
            emailService.sendPasswordResetEmail(user, token);
        } else {
            throw new EmailNotFoundException();
        }
    }

    public void resetPassword(PasswordResetBody body) {
        try {
            String email = jwtService.getResetPasswordEmail(body.getToken());
            Optional<User> opUser = userDAO.findByEmailIgnoreCase(email);
            if (opUser.isPresent()) {
                User user = opUser.get();
                user.setPassword(encryptionService.encryptPassword(body.getPassword()));
                userDAO.save(user);
            }
        } catch(TokenExpiredException e) {
            throw e;
        }
    }

    public void updateToken(User user, String newToken) {
        try {
            String oldToken = user.getPushToken();

            if(newToken == oldToken) return;
            
            user.setPushToken(newToken);
            userDAO.save(user);

            List<RoomMembers> roomMembers = roomMembersDAO.findByUser(user);

            for(RoomMembers member: roomMembers) {
                String topic = "room" + member.getRoom().getId().toString();

                if(oldToken != null) {
                    List<String> oldTokenList = new ArrayList<String>() {{
                        add(oldToken);
                    }};
                    firebaseMessagingService.unsubscribeFromTopic(oldTokenList, topic);
                }
                
                List<String> newTokenList = new ArrayList<>() {{
                    add(newToken);
                }};
                firebaseMessagingService.subscribeToTopic(newTokenList, topic);
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTokenHelper(TokenUpdateBody body) throws UserNotFound {
        User user = userDAO.findByEmailIgnoreCase(body.getUserEmail()).orElseThrow(UserNotFound::new);
        updateToken(user, body.getToken());
    }

    public String toggleRegistration(User user) {
        try{
            if(user == null) return "TOKEN_MISSING";
            if(user.isRegistered()) return "ALREADY_REGISTERED"; 
            if(!user.isEmailVerified()) return "NOT_EMAIL_VERIFIED";
            user.setRegistered(true);
            userDAO.save(user);
            return "SUCCESS";
        } catch(Exception e) {
            return "FAILED";
        }
    }

    public ImagesUploadRes updateDp(User user, MultipartFile file, boolean delete) throws UserNotFound, MediaUploadFailed{
        try {
            if(user == null) throw new UserNotFound();
            if(user.getImagePath() != null) {
                Optional<Media> oldImage = mediaDAO.findByUrl(user.getImagePath());
                    if(oldImage.isPresent()){
                        mediaDAO.delete(oldImage.get());
                        mediaUtil.deleteImage(oldImage.get().getPublicId());
                    }
            }
            ImagesUploadRes res = new ImagesUploadRes();
            if(delete) {
                user.setImagePath(null);
                userDAO.save(user);
                res.setMessage("DP successfully deleted!");
                return res;
            } else {
                res = mediaUtil.uploadFile(file, "user-dp");
                Media newImage = new Media();
                newImage.setPublicId(res.getPublicId());
                newImage.setUrl(res.getUrl());
                mediaDAO.save(newImage);
    
                user.setImagePath(res.getUrl());
                userDAO.save(user);
                return res;
            }
        } catch(IOException e) {
            throw new MediaUploadFailed(e.getMessage());
        }
    }

    public void addUserToWorkshopRooms(User user) {
        List<Long> roomIds = roomsDAO.getRoomIdsByType("workshop");
        roomIds.forEach(roomId -> {
            try {
                MembersList body = new MembersList();
                body.setRoom(roomId);
                List<Members> list = new ArrayList<>();
                list.add(new Members(user.getEmail(), "user"));
                body.setMembers(list);
                roomMembersService.addMembers(body);
            } catch(RoomDoesNotExist | InterruptedException | ExecutionException e) {
                System.out.println(e.getMessage());
            }
        });
    }
    
}
