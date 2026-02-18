package com.authserver.authserver.user.dependency;

public interface EmailServiceInterface {
    public abstract Boolean sendEmail(String to, String subject, String body);   
}
