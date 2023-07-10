package com.parkmobile.server.service.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Set the host and port for Gmail SMTP
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        // Set the username and password for authentication
        mailSender.setUsername("khitemmathlouthi@gmail.com");
        mailSender.setPassword("mvzdqojjyxozvicz");

        // Set additional properties like TLS/SSL configuration
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }
}
