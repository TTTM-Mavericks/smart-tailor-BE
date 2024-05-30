package com.smart.tailor.constant;

public class MessageConstant {
    /**
     * ERROR MESSAGE
     */
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL SERVER ERROR";
    public static final String BAD_REQUEST = "BAD REQUEST";
    public static final String MISSING_ARGUMENT = "MISSING ARGUMENT";

    /**
     * FORMATION ERROR
     */
    public static final String INVALID_EMAIL = "Invalid Email!";
    public static final String INVALID_PASSWORD = "Invalid Password!";
    public static final String INVALID_PHONE_NUMBER_FORMAT = "Invalid phone Number format!";


    /**
     * ROLE
     */
    public static final String GET_ALL_ROLES_SUCCESSFULLY = "Get All Roles Successfully!";
    public static final String ROLE_LIST_IS_EMPTY = "Role List Is Empty!";


    /**
     * AUTHENTICATION
     */
    /*    SUCCESS     */
    public static final String REGISTER_NEW_USER_SUCCESSFULLY = "Register New User Successfully!";
    public static final String SEND_MAIL_FOR_UPDATE_PASSWORD_SUCCESSFULLY = "Check Email To Update Password!";
    public static final String UPDATE_PASSWORD_SUCCESSFULLY = "Update Password Successfully!";
    public static final String LOGIN_SUCCESSFULLY = "Login Successfully!";
    public static final String ACCOUNT_VERIFIED_SUCCESSFULLY = "Account Verified Successfully!";
    public static final String REFRESH_TOKEN_SUCCESSFULLY = "Refresh Token Successfully!";
    /*    FAIL     */
    public static final String DUPLICATE_REGISTERED_EMAIL = "Duplicate Registered Email!";
    public static final String INVALID_VERIFICATION_TOKEN = "Invalid Verification Token!";
    public static final String INVALID_EMAIL_OR_PASSWORD = "Invalid Email Or Password!";
    public static final String UPDATE_PASSWORD_FAILED = "Failed To Update Password!";
    public static final String REGISTER_NEW_USER_FAILED = "Failed To Register New User!";
    public static final String REFRESH_TOKEN_FAILED = "Failed To Refresh Token!";

    /**
     * USER
     */
    /*    FAIL     */
    public static final String USER_NOT_FOUND = "User Not Found!";
    public static final String DUPLICATE_REGISTER_PHONE = "Duplicate Register Phone!";
}
