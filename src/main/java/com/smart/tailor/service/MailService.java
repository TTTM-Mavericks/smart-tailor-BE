package com.smart.tailor.service;

public interface MailService {
   void verifyAccount(String email, String emailSubject, String verificationUrl);
}
