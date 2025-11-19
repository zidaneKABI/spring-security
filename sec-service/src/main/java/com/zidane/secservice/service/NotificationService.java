package com.zidane.secservice.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.zidane.secservice.sec.entities.Validation;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NotificationService {


    private JavaMailSender mailSender;    
    
    
    public void sendValidationEmail(Validation validation) {
       
        // Implementation to send email using mailSender
        // You would typically create a MimeMessage, set the recipient, subject, and body, then send it.    
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(validation.getAppUser().getEmail());
        message.setFrom("no-replay@zidanetech.com");
        message.setSubject("Votre code de validation");
        message.setText("Bonjour " + validation.getAppUser().getName() + ",\n\n"
                + "Voici votre code de validation : " + validation.getCode() + "\n"
                + "Ce code expirera le : " + validation.getDateExpiration() + "\n\n"
                + "Cordialement,\n"
                + "L'équipe de support");
        mailSender.send(message);


    
    
    }

}
