package com.authserver.authserver.communication.services;

import org.springframework.stereotype.Service;

import com.authserver.authserver.communication.util.EmailUtil;
import com.authserver.authserver.user.dependency.EmailServiceInterface;

@Service
public class EmailService implements EmailServiceInterface {

    private final EmailUtil emailUtil;

    EmailService(EmailUtil emailUtil) {
        this.emailUtil = emailUtil;
    }

    @Override
    public Boolean sendEmail(String to, String subject, String body) {
        return emailUtil.sendEmail(to, subject, body, null, null, body);
    }
   
}
