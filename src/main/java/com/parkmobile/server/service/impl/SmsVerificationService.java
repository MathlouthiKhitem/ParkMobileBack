package com.parkmobile.server.service.impl;

import com.twilio.type.PhoneNumber;
import java.util.Random;
import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.twilio.rest.api.v2010.account.Message;

import java.util.Random;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SmsVerificationService {

    private final String ACCOUNT_SID;
    private final String AUTH_TOKEN;
    private final String TWILIO_PHONE_NUMBER;

    public SmsVerificationService(@Value("${twilio.account.sid}") String accountSid,
                                  @Value("${twilio.auth.token}") String authToken,
                                  @Value("${twilio.phone.number}") String twilioPhoneNumber) {
        ACCOUNT_SID = accountSid;
        AUTH_TOKEN = authToken;
        TWILIO_PHONE_NUMBER = twilioPhoneNumber;
    }

    public String sendVerificationCode(String phoneNumber) {
        // Generate a random 6-digit verification code
        String verificationCode = generateVerificationCode();

        // Compose the SMS message
        String messageBody = "Your verification code is: " + verificationCode;

        // Send the SMS
        Message message = Message.creator(
            new PhoneNumber(phoneNumber),
            new PhoneNumber(TWILIO_PHONE_NUMBER),
            messageBody
        ).create();
        return verificationCode;
        // You can add additional logging or error handling here
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int verificationCode = 100000 + random.nextInt(900000);
        return String.valueOf(verificationCode);
    }
}
