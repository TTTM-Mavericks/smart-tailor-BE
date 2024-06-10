package com.smart.tailor.service;

public interface ThymeleafService {
    String createThymeleafForVerifyAccount(String email, String verificationUrl);

    String createThymeleafForResetPassword(String email, String verificationUrl);

    String createThymeleafForChangePassword(String email, String verificationUrl);
}
