package com.dev.zine.api.controllers.reset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.dev.zine.service.JWTService;
import com.dev.zine.service.UserService;

@Controller
public class MiscController {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserService userService;
    @Value("${app.frontend.url}")
    private String prodUrl;
    @Value("${app.environment}")
    private String env;
    @Value("${app.dev.url}")
    private String devUrl;

    @GetMapping("/reset-password")
    public String loadPage(@RequestParam("token") String token, Model model) {
        try {
            String email = jwtService.getResetPasswordEmail(token);
            model.addAttribute("email", email);
            model.addAttribute("token", token);
            if(env.equals("production")) {
                model.addAttribute("url", prodUrl);
            } else {
                model.addAttribute("url", devUrl);
            }
            return "reset-pass";
        } catch(TokenExpiredException e) {
            return "token-expired";
        }
    }

    @GetMapping("/auth/verify")
    public String verifyEmail(@RequestParam String token, Model model) {
        if (userService.verifyUser(token)) {
            model.addAttribute("message", "Thank you for registering with us! Log into our app or website to continue.");
            return "verify-email";
        } else {
            model.addAttribute("message", "Error verifying email address. Please try again.");
            return "verify-email";
        }
    }
    
    
}
