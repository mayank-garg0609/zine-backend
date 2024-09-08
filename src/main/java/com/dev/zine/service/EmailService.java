package com.dev.zine.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.dev.zine.exceptions.EmailFailureException;
import com.dev.zine.model.User;
import com.dev.zine.model.VerificationToken;

/**
 * Service for handling emails being sent.
 */
@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Value("${app.frontend.url}")
    private String prodUrl;
    @Value("${app.environment}")
    private String env;
    @Value("${app.dev.url}")
    private String devUrl;

    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    private SimpleMailMessage makeMailMessage() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromAddress);
        return simpleMailMessage;
    }

    public void sendVerificationEmail(VerificationToken verificationToken) throws EmailFailureException {
        String url;
        if(env.equals("production")) {
            url = prodUrl;
        } else {
            url = devUrl;
        }
        SimpleMailMessage message = makeMailMessage();
        message.setTo(verificationToken.getUser().getEmail());
        message.setSubject("Verify your email to active your account.");
        message.setText("Please follow the link below to verify your email to active your account.\n" +
                url + "/auth/verify?token=" + verificationToken.getToken());
        try {
            System.out.println("inside send mail");
            javaMailSender.send(message);
        } catch (MailException ex) {
            throw new EmailFailureException();
        }
    }

    public void sendPasswordResetEmail(User user, String token) throws EmailFailureException {
        String url;
        if(env.equals("production")) {
            url = prodUrl;
        } else {
            url = devUrl;
        }
        SimpleMailMessage message = makeMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Your password reset request link.");
        message.setText("You requested a password reset on our website. Please " +
                "find the link below to be able to reset your password.\n" + url +
                "/reset-password?token=" + token);
        try {
            javaMailSender.send(message);
        } catch (MailException ex) {
            throw new EmailFailureException();
        }
    }

}