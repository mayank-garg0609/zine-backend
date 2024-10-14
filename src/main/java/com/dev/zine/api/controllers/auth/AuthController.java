package com.dev.zine.api.controllers.auth;

import jakarta.validation.Valid;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.zine.api.model.auth.LoginBody;
import com.dev.zine.api.model.auth.LoginResponse;
import com.dev.zine.api.model.auth.PasswordResetBody;
import com.dev.zine.api.model.auth.RegistrationBody;
import com.dev.zine.exceptions.EmailFailureException;
import com.dev.zine.exceptions.EmailNotFoundException;
import com.dev.zine.exceptions.IncorrectPasswordException;
import com.dev.zine.exceptions.UserAlreadyExistsException;
import com.dev.zine.exceptions.UserNotFound;
import com.dev.zine.exceptions.UserNotVerifiedException;
import com.dev.zine.model.User;
import com.dev.zine.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationBody registrationBody) {
        System.out.println(registrationBody);
        try {
            userService.registerUser(registrationBody);
            return ResponseEntity.ok().body(Map.of("message","User created successfully"));
        } catch (UserAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message","User already exists"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginBody loginBody) {
        String jwt = null;
        try {
            jwt = userService.loginUser(loginBody);
        } catch (UserNotVerifiedException ex) {
            LoginResponse response = new LoginResponse();
            response.setSuccess(false);
            String reason = "user_not_verified";
            if (ex.isNewEmailSent()) {
                reason += "_email_resent";
            }
            response.setFailureReason(reason);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } catch (EmailFailureException ex) {
            LoginResponse response = new LoginResponse();
            response.setSuccess(false);
            response.setFailureReason(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch(UserNotFound | IncorrectPasswordException e) {
            LoginResponse response = new LoginResponse();
            response.setSuccess(false);
            response.setFailureReason(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            LoginResponse response = new LoginResponse();
            response.setJwt(jwt);
            response.setSuccess(true);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/me")
    public User getLoggedInUserProfile(@AuthenticationPrincipal User user) {
        return user;
    }

    @PostMapping("/forgot")
    public ResponseEntity<Object> forgotPassword(@RequestParam String email) {
        try {
            userService.forgotPassword(email);
            return ResponseEntity.ok().build();
        } catch (EmailNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("messsage", ex.getMessage()));
        } catch (EmailFailureException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetBody body) {
        System.out.println(body.getPassword());
        try{
            userService.resetPassword(body);
            return ResponseEntity.ok().body(Map.of("status","success"));
        } catch(Exception e){
            return ResponseEntity.ok().body(Map.of("status","failed"));
        }
        
    }

}