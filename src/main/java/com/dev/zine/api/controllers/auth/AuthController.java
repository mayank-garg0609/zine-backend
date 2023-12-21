package com.dev.zine.api.controllers.auth;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.zine.api.controllers.model.RegistrationBody;
import com.dev.zine.exceptions.UserAlreadyExistsException;
import com.dev.zine.service.userService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private userService userService;

    public AuthController(userService userService) {
        this.userService = userService;
    }

    /**
     * Post Mapping to handle registering users.
     *
     * @param registrationBody The registration information.
     * @return Response to front end.
     */
    @PostMapping("/register")
    public ResponseEntity registerUser(@Valid @RequestBody RegistrationBody registrationBody) {
        System.out.println(registrationBody);
        try {
            userService.registerUser(registrationBody);
            return ResponseEntity.ok().build();
        } catch (UserAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

}