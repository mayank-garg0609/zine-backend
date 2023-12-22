package com.dev.zine.service;

import org.springframework.stereotype.Service;

import com.dev.zine.api.model.LoginBody;
import com.dev.zine.api.model.RegistrationBody;
import com.dev.zine.dao.UserDAO;
import com.dev.zine.dao.VerificationTokenDAO;
import com.dev.zine.exceptions.EmailFailureException;
import com.dev.zine.exceptions.UserAlreadyExistsException;
import com.dev.zine.exceptions.UserNotVerifiedException;
import com.dev.zine.model.User;
import com.dev.zine.model.VerificationToken;

import jakarta.transaction.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service
public class UserService {

    private UserDAO userDAO;
    private VerificationTokenDAO verificationTokenDAO;

    private EncryptionService encryptionService;

    private JWTService jwtService;
    private EmailService emailService;

    public UserService(UserDAO userDAO, EncryptionService encryptionService, JWTService jwtService,
            EmailService emailService, VerificationTokenDAO verificationTokenDAO) {
        this.userDAO = userDAO;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.verificationTokenDAO = verificationTokenDAO;
    }

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
        return userDAO.save(user);
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
        user.getVerificationTokens().add(verificationToken);
        return verificationToken;
    }

    @Transactional
    public String loginUser(LoginBody loginBody) throws UserNotVerifiedException, EmailFailureException {
        Optional<User> opUser = userDAO.findByEmailIgnoreCase(loginBody.getEmail());
        if (opUser.isPresent()) {
            User user = opUser.get();
            if (encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())) {
                if (user.isEmailVerified()) {
                    return jwtService.generateJWT(user);
                } else {
                    List<VerificationToken> verificationTokens = user.getVerificationTokens();
                    boolean resend = verificationTokens.isEmpty() ||
                            verificationTokens.get(0).getCreatedTimestamp()
                                    .before(new Timestamp(System.currentTimeMillis() - (60 * 60 * 1000)));
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

}
