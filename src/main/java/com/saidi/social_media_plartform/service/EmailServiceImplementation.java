package com.saidi.social_media_plartform.service;

import com.saidi.social_media_plartform.utils.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImplementation implements EmailService{
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${VERIFY_EMAIL_HOST}")
    private String host;
    private JavaMailSender javaMailSender;

    @Autowired
    public EmailServiceImplementation(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    @Async
    public void sentEmailToUser(String name, String to, String token) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("Saidi Social Media Platform Account Verification");
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(EmailUtil.getEmailMessage(name, host, token));
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
