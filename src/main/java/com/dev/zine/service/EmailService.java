package com.dev.zine.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.dev.zine.exceptions.EmailFailureException;
import com.dev.zine.model.User;
import com.dev.zine.model.VerificationToken;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

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
        String verifyLink =  url + "/auth/verify?token=" + verificationToken.getToken();
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
    
            String htmlMsg = "<div style=\"font-family: Arial, sans-serif; text-align: center;\">"
                    + "<h2 style=\"color: #333;\">Email Verification</h2>"
                    + "<p style=\"font-size: 14px; color: #555;\">Thank you for registering on our website.</p>"
                    + "<p style=\"font-size: 14px; color: #555;\">Please click the button below to verify your email address:</p>"
                    + "<div style=\"margin: 20px;\">"
                    + "<a href=\"" + verifyLink + "\" style=\"background-color: #0C72B0; color: white; padding: 10px 20px; "
                    + "text-align: center; text-decoration: none; display: inline-block; border-radius: 5px; font-size: 16px;\">"
                    + "Verify Email</a>"
                    + "</div>"
                    + "<p style=\"font-size: 12px; color: #999; margin-top: 20px;\">If you did not create this account, please ignore this email.</p>"
                    + "</div>";

            helper.setTo(verificationToken.getUser().getEmail());
            helper.setSubject("Verify Your Email Address");
            helper.setText(htmlMsg, true);

            javaMailSender.send(mimeMessage);
        } catch (MailException ex) {
            throw new EmailFailureException();
        } catch (MessagingException e) {
            throw new EmailFailureException();
        }
    }

    public void sendPasswordResetEmail(User user, String token) throws EmailFailureException {
        String url;
        if (env.equals("production")) {
            url = prodUrl;
        } else {
            url = devUrl;
        }
    
        String verifyLink = url + "/reset-password?token=" + token;
    
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
    
            String htmlMsg = "<div style=\"font-family: Arial, sans-serif; text-align: center;\">"
                    + "<h2 style=\"color: #333;\">Password Reset Request</h2>"
                    + "<p style=\"font-size: 14px; color: #555;\">You requested a password reset on our website.</p>"
                    + "<p style=\"font-size: 14px; color: #555;\">Please click the button below to reset your password:</p>"
                    + "<div style=\"margin: 20px;\">"
                    + "<a href=\"" + verifyLink + "\" style=\"background-color: #0C72B0; color: white; padding: 10px 20px; "
                    + "text-align: center; text-decoration: none; display: inline-block; border-radius: 5px; font-size: 16px;\">"
                    + "Reset Password</a>"
                    + "</div>"
                    + "<p style=\"font-size: 12px; color: #999; margin-top: 20px;\">If you did not request this, please ignore this email.</p>"
                    + "</div>";
    
            helper.setTo(user.getEmail());
            helper.setSubject("Your password reset request link");
            helper.setText(htmlMsg, true);
    
            javaMailSender.send(mimeMessage);
        } catch (MailException ex) {
            throw new EmailFailureException();
        } catch (MessagingException e) {
            throw new EmailFailureException();
        }
    }
}