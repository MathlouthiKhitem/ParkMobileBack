package com.parkmobile.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
public class EmailVerificationService {
    private JavaMailSender mailSender;

    @Autowired
    public EmailVerificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

//    public void sendVerificationCode(String email) {
//        String verificationCode = generateVerificationCode();
//
//        // Save the verification code for future validation, e.g., in the database
//
//        try {
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//            helper.setTo(email);
//            helper.setSubject("Email Verification");
//            helper.setText("Your verification code is: " + verificationCode);
//
//            mailSender.send(message);
//        } catch (MessagingException e) {
//            // Handle exception appropriately
//            e.printStackTrace();
//        }
//    }
public String sendVerificationCode(String email) {
    String verificationCode = generateVerificationCode();

    // Save the verification code for future validation, e.g., in the database

    try {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("Email Verification");
        helper.setText("Your verification code is: " + verificationCode);

        mailSender.send(message);
    } catch (MessagingException e) {
        // Handle exception appropriately
        e.printStackTrace();
    }

    return verificationCode;
}
    private String generateVerificationCode() {
        // Generate a random verification code here
        // For simplicity, let's assume it's a 6-digit code
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
