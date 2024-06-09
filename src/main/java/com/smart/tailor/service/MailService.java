package com.smart.tailor.service;

public interface MailService {
   void sendMailVerifyAccount(String emailTo, String emailSubject, String verificationUrl);

   void sendMailResetPassword(String emailTo, String emailSubject, String verificationUrl);

   void sendMailChangePassword(String emailTo, String emailSubject, String verificationUrl);
}
