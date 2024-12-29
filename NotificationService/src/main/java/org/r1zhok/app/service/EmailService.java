package org.r1zhok.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.r1zhok.app.dto.EmailDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${mail.sender-mail-address}")
    private String emailSender;

    public void sendEmail(EmailDetails emailDetails){
        try {
            SimpleMailMessage mailMsg = new SimpleMailMessage();
            mailMsg.setFrom(emailSender);
            mailMsg.setTo(emailDetails.supplier());
            mailMsg.setText(emailDetails.messageBody());
            mailMsg.setSubject(emailDetails.subject());
            javaMailSender.send(mailMsg);
        }catch (MailException exception){
            log.debug("Failure occurred while sending email");
        }
    }
}