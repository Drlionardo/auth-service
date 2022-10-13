package com.drlionardo.authserver.service;

import com.drlionardo.authserver.property.EmailProperties;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {
    private final MailSender emailSender;
    private final EmailProperties emailProperties;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailProperties.getFrom());
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
