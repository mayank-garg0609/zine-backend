package com.dev.zine.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.dev.zine.exceptions.EmailFailureException;
import com.dev.zine.model.User;
import com.dev.zine.model.VerificationToken;
import com.dev.zine.model.form.Form;
import com.dev.zine.model.form.Question;
import com.dev.zine.model.form.Response;

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
    @Autowired
    private TemplateEngine templateEngine;

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
        System.out.println("[EmailService] === sendVerificationEmail START ===");
        System.out.println("[EmailService] Recipient: " + verificationToken.getUser().getEmail());
        System.out.println("[EmailService] Environment: " + env);
        System.out.println("[EmailService] From address: " + fromAddress);
        System.out.println("[EmailService] Production URL: " + prodUrl);
        System.out.println("[EmailService] Dev URL: " + devUrl);
        
        String url;
        if (env.equals("production")) {
            url = prodUrl;
            System.out.println("[EmailService] Using PRODUCTION URL");
        } else {
            url = devUrl;
            System.out.println("[EmailService] Using DEVELOPMENT URL");
        }
        String verifyLink = url + "/auth/verify?token=" + verificationToken.getToken();
        System.out.println("[EmailService] Verification link: " + verifyLink);
        
        try {
            System.out.println("[EmailService] Creating MIME message...");
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlMsg = "<div style=\"font-family: Arial, sans-serif; text-align: center;\">"
                    + "<h2 style=\"color: #333;\">Email Verification</h2>"
                    + "<p style=\"font-size: 14px; color: #555;\">Thank you for registering on our website.</p>"
                    + "<p style=\"font-size: 14px; color: #555;\">Please click the button below to verify your email address:</p>"
                    + "<div style=\"margin: 20px;\">"
                    + "<a href=\"" + verifyLink
                    + "\" style=\"background-color: #0C72B0; color: white; padding: 10px 20px; "
                    + "text-align: center; text-decoration: none; display: inline-block; border-radius: 5px; font-size: 16px;\">"
                    + "Verify Email</a>"
                    + "</div>"
                    + "<p style=\"font-size: 12px; color: #999; margin-top: 20px;\">If you did not create this account, please ignore this email.</p>"
                    + "</div>";

            helper.setFrom(fromAddress);
            helper.setTo(verificationToken.getUser().getEmail());
            helper.setSubject("Verify Your Email Address");
            helper.setText(htmlMsg, true);

            System.out.println("[EmailService] Sending verification email via JavaMailSender...");
            javaMailSender.send(mimeMessage);
            System.out.println("[EmailService] ✓ Verification email sent successfully!");
        } catch (MailException ex) {
            System.out.println("[EmailService] ✗ MailException in sendVerificationEmail!");
            System.out.println("[EmailService] Exception class: " + ex.getClass().getName());
            System.out.println("[EmailService] Error message: " + ex.getMessage());
            if (ex.getCause() != null) {
                System.out.println("[EmailService] Root cause: " + ex.getCause().getMessage());
                System.out.println("[EmailService] Root cause class: " + ex.getCause().getClass().getName());
            }
            ex.printStackTrace();
            throw new EmailFailureException();
        } catch (MessagingException e) {
            System.out.println("[EmailService] ✗ MessagingException in sendVerificationEmail!");
            System.out.println("[EmailService] Exception class: " + e.getClass().getName());
            System.out.println("[EmailService] Error message: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("[EmailService] Root cause: " + e.getCause().getMessage());
            }
            e.printStackTrace();
            throw new EmailFailureException();
        }
        System.out.println("[EmailService] === sendVerificationEmail END ===");
    }

    public void sendPasswordResetEmail(User user, String token) throws EmailFailureException {
        System.out.println("[EmailService] === sendPasswordResetEmail START ===");
        System.out.println("[EmailService] Recipient: " + user.getEmail());
        System.out.println("[EmailService] Environment: " + env);
        System.out.println("[EmailService] From address: " + fromAddress);
        System.out.println("[EmailService] Production URL: " + prodUrl);
        System.out.println("[EmailService] Dev URL: " + devUrl);
        
        String url;
        if (env.equals("production")) {
            url = prodUrl;
            System.out.println("[EmailService] Using PRODUCTION URL");
        } else {
            url = devUrl;
            System.out.println("[EmailService] Using DEVELOPMENT URL");
        }

        String verifyLink = url + "/reset-password?token=" + token;
        System.out.println("[EmailService] Reset link: " + verifyLink);

        try {
            System.out.println("[EmailService] Creating MIME message...");
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlMsg = "<div style=\"font-family: Arial, sans-serif; text-align: center;\">"
                    + "<h2 style=\"color: #333;\">Password Reset Request</h2>"
                    + "<p style=\"font-size: 14px; color: #555;\">You requested a password reset on our website.</p>"
                    + "<p style=\"font-size: 14px; color: #555;\">Please click the button below to reset your password:</p>"
                    + "<div style=\"margin: 20px;\">"
                    + "<a href=\"" + verifyLink
                    + "\" style=\"background-color: #0C72B0; color: white; padding: 10px 20px; "
                    + "text-align: center; text-decoration: none; display: inline-block; border-radius: 5px; font-size: 16px;\">"
                    + "Reset Password</a>"
                    + "</div>"
                    + "<p style=\"font-size: 12px; color: #999; margin-top: 20px;\">If you did not request this, please ignore this email.</p>"
                    + "</div>";

            helper.setFrom(fromAddress);
            helper.setTo(user.getEmail());
            helper.setSubject("Your password reset request link");
            helper.setText(htmlMsg, true);

            System.out.println("[EmailService] Sending password reset email via JavaMailSender...");
            javaMailSender.send(mimeMessage);
            System.out.println("[EmailService] ✓ Password reset email sent successfully!");
        } catch (MailException ex) {
            System.out.println("[EmailService] ✗ MailException in sendPasswordResetEmail!");
            System.out.println("[EmailService] Exception class: " + ex.getClass().getName());
            System.out.println("[EmailService] Error message: " + ex.getMessage());
            if (ex.getCause() != null) {
                System.out.println("[EmailService] Root cause: " + ex.getCause().getMessage());
                System.out.println("[EmailService] Root cause class: " + ex.getCause().getClass().getName());
            }
            ex.printStackTrace();
            throw new EmailFailureException();
        } catch (MessagingException e) {
            System.out.println("[EmailService] ✗ MessagingException in sendPasswordResetEmail!");
            System.out.println("[EmailService] Exception class: " + e.getClass().getName());
            System.out.println("[EmailService] Error message: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("[EmailService] Root cause: " + e.getCause().getMessage());
            }
            e.printStackTrace();
            throw new EmailFailureException();
        }
        System.out.println("[EmailService] === sendPasswordResetEmail END ===");
    }

    public void sendUserFormResponse(Form form, User user) throws EmailFailureException {
        try {
            Map<String, String> questionToAnswer = new LinkedHashMap<>();
            List<Question> questions = form.getQuestions();
            for (Question question : questions) {
                for (Response response : question.getResponses()) {
                    if(response.getUser().equals(user)) {
                        if(question.getType().equals("text")) {
                            System.out.println(question.getText().getContent()); 
                            System.out.println(response.getTextResponse().getContent());
                            questionToAnswer.put(question.getText().getContent(), response.getTextResponse().getContent());
                        }
                        else if(question.getType().equals("poll")) {
                            System.out.println(question.getPoll().getTitle());
                            System.out.println(response.getPollResponse().getOption().getValue());
                            questionToAnswer.put(question.getPoll().getTitle(), response.getPollResponse().getOption().getValue());
                        }
    
                    }
                }
            }

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            
            StringBuilder emailContent = new StringBuilder();
            emailContent.append("<!DOCTYPE html>")
                       .append("<html>")
                       .append("<head>")
                       .append("<style>")
                       .append("body { font-family: 'Arial', sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }")
                       .append(".header { background: linear-gradient(135deg, #1386cc, #0C72B0F2); color: white; padding: 30px; border-radius: 10px; margin-bottom: 30px; }")
                       .append(".header h1 { margin: 0; font-size: 24px; font-weight: 600; }")
                       .append(".header p { margin: 10px 0 0; opacity: 0.9; }")
                       .append(".response-item { background: #f8f9fa; border-radius: 8px; padding: 20px; margin-bottom: 15px; box-shadow: 0 2px 4px rgba(0,0,0,0.05); }")
                       .append(".question { color: #1386cc; font-weight: 600; margin-bottom: 8px; font-size: 16px; }")
                       .append(".answer { color: #374151; font-size: 15px; }")
                       .append(".footer { margin-top: 30px; padding-top: 20px; border-top: 1px solid #e5e7eb; color: #0C72B0F2; font-size: 14px; }")
                       .append("</style>")
                       .append("</head>")
                       .append("<body>")
                       .append("<div class='header'>")
                       .append("<h1>Thank You for Your Submission</h1>")
                       .append("<p>Here's a summary of your responses</p>")
                       .append("</div>");

            for (Map.Entry<String, String> entry : questionToAnswer.entrySet()) {
                emailContent.append("<div class='response-item'>")
                           .append("<div class='question'>")
                           .append(entry.getKey())
                           .append("</div>")
                           .append("<div class='answer'>")
                           .append(entry.getValue())
                           .append("</div>")
                           .append("</div>");
            }
            
            emailContent.append("<div class='footer'>")
                       .append("<p>This is an automated response. Please do not reply to this email.</p>")
                       .append("</div>")
                       .append("</body>")
                       .append("</html>");
            
            helper.setFrom(fromAddress);
            helper.setSubject("Thank you for your response!");
            helper.setTo(user.getEmail());
            helper.setText(emailContent.toString(), true);

            System.out.println(emailContent.toString());

            javaMailSender.send(mimeMessage);

        } catch (MailException ex) {
            throw new EmailFailureException();
        } catch (MessagingException e) {
            throw new EmailFailureException();
        }
    }

}