package com.smart.tailor.constant;

public class APIConstant {
    /**
     * Default API
     */
    public static final String API = "/api/v1";

    /**
     * Authentication API
     */
    public class AuthenticationAPI {

        public static final String AUTHENTICATION = APIConstant.API + "/auth";
        public static final String REGISTER = "/register";
        public static final String VERIFY = "/verify";
        public static final String VERIFY_PASSWORD = "/verify-password";
        public static final String FORGOT_PASSWORD = "/forgot-password";
        public static final String UPDATE_PASWORD = "/update-password";
        public static final String GOOGLE_REGISTER = "/google-register";
        public static final String FACEBOOK_REGISTER = "/facebook-register";
        public static final String LOGIN = "/login";
        public static final String GOOGLE_LOGIN = "/google-login";
        public static final String FACEBOOK_LOGIN = "/facebook-login";
        public static final String LOG_OUT = "/log-out";
        public static final String REFRESH_TOKEN = "/refresh-token";
    }

    /**
     * Role API
     */
    public class RoleAPI {
        public static final String ROLE = APIConstant.API + "/roles";
        public static final String GET_ALL_ROLES = "/get-all-roles";
        public static final String ADD_NEW_ROLES = "/add-new-roles";
    }
}
