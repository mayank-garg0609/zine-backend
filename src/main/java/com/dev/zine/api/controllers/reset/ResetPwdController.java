package com.dev.zine.api.controllers.reset;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestParam;

import com.dev.zine.service.JWTService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.dev.zine.api.model.auth.PasswordResetBody;


@Controller
public class ResetPwdController {
    JWTService jwtService;
    ResetPwdController(JWTService jwtService){
        this.jwtService = jwtService;
    }
    @GetMapping("/reset-password")
    public String loadPage(@RequestParam("token") String token, Model model) {
        String email = jwtService.getResetPasswordEmail(token);
        model.addAttribute("email", email);
        model.addAttribute("token", token);
        return "resetpass";
    }
    
    
}
