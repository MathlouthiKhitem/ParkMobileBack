package com.parkmobile.server.service;

import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {

    public TwilioConfig(@Value("${twilio.account.sid}") String accountSid,
                        @Value("${twilio.auth.token}") String authToken) {
        Twilio.init(accountSid, authToken);
    }

}
