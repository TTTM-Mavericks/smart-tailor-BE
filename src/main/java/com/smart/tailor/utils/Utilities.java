package com.smart.tailor.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()\\-+])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean isValidAlphabeticString(String input) {
        String alphabeticRegex = "^[a-zA-Z]+$";
        Pattern pattern = Pattern.compile(alphabeticRegex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public static boolean isValidVietnamesePhoneNumber(String phoneNumber) {
        String vietnamesePhoneNumberRegex = "^0\\d{9}$";
        Pattern pattern = Pattern.compile(vietnamesePhoneNumberRegex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean isValidDate(String dateStr, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false); // Đặt giá trị này để không cho phép chấp nhận các ngày không hợp lệ
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isValidString(String str){
        return !str.isBlank() && !str.isEmpty();
    }
}
