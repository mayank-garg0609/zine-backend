package com.dev.zine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.zine.api.model.auth.LoginBody;
import com.dev.zine.api.model.auth.PasswordResetBody;
import com.dev.zine.api.model.auth.RegistrationBody;
import com.dev.zine.api.model.user.TokenUpdateBody;
import com.dev.zine.dao.RoleDAO;
import com.dev.zine.dao.RoomMembersDAO;
import com.dev.zine.dao.UserDAO;
import com.dev.zine.dao.UserToRoleDAO;
import com.dev.zine.dao.VerificationTokenDAO;
import com.dev.zine.exceptions.EmailFailureException;
import com.dev.zine.exceptions.EmailNotFoundException;
import com.dev.zine.exceptions.UserAlreadyExistsException;
import com.dev.zine.exceptions.UserNotFound;
import com.dev.zine.exceptions.UserNotVerifiedException;
import com.dev.zine.model.Role;
import com.dev.zine.model.RoomMembers;
import com.dev.zine.model.User;
import com.dev.zine.model.UserToRole;
import com.dev.zine.model.VerificationToken;

import jakarta.transaction.Transactional;

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

        String findRole = user.getEmail().substring(0, 4);
        Role role = roleDAO.findByPermission(findRole).orElse(null);
        if(role != null) {
            UserToRole roleMapping = new UserToRole();
            roleMapping.setRole(role);
            roleMapping.setUser(user);
            userToRoleDAO.save(roleMapping);
        }

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

    @Transactional
    public String loginUser(LoginBody loginBody) throws UserNotVerifiedException, EmailFailureException {


        Optional<User> opUser = userDAO.findByEmailIgnoreCase(loginBody.getEmail()); // checks if user exists
        if (opUser.isPresent()) {
            User user = opUser.get();
            if (encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())) { //verify password
                if (user.isEmailVerified()) {
                    System.out.println(loginBody.getPushToken());
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
            }
        }
        return null;
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
        String email = jwtService.getResetPasswordEmail(body.getToken());
        Optional<User> opUser = userDAO.findByEmailIgnoreCase(email);
        if (opUser.isPresent()) {
            User user = opUser.get();
            user.setPassword(encryptionService.encryptPassword(body.getPassword()));
            userDAO.save(user);
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

}
