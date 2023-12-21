package com.dev.zine.service;

import org.springframework.stereotype.Service;

import com.dev.zine.api.controllers.model.RegistrationBody;
import com.dev.zine.dao.UserDAO;
import com.dev.zine.dao.VerificationTokenDAO;
import com.dev.zine.exceptions.UserAlreadyExistsException;
import com.dev.zine.model.User;

@Service
public class userService {

    private UserDAO userDAO;
    private VerificationTokenDAO verificationTokenDAO;

    private EncryptionService encryptionService;

    private JWTService jwtService;

    public userService(UserDAO userDAO) {
        this.userDAO = userDAO;
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

        user.setPassword(registrationBody.getPassword());
        return userDAO.save(user);
    }
}
