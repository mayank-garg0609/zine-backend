package com.dev.zine.service;

import org.springframework.stereotype.Service;

import com.dev.zine.api.model.LoginBody;
import com.dev.zine.api.model.RegistrationBody;
import com.dev.zine.dao.UserDAO;
import com.dev.zine.dao.VerificationTokenDAO;
import com.dev.zine.exceptions.UserAlreadyExistsException;
import com.dev.zine.model.User;
import java.util.*;

@Service
public class UserService {

    private UserDAO userDAO;
    private VerificationTokenDAO verificationTokenDAO;

    private EncryptionService encryptionService;

    private JWTService jwtService;

    public UserService(UserDAO userDAO, EncryptionService encryptionService, JWTService jwtService) {
        this.userDAO = userDAO;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
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
        return userDAO.save(user);
    }

    public String loginUser(LoginBody loginBody) {
        Optional<User> opUser = userDAO.findByEmailIgnoreCase(loginBody.getEmail());
        if (opUser.isPresent()) {
            User user = opUser.get();
            if (encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())) {
                return jwtService.generateJWT(user);
            }
        }
        return null;
    }

}
