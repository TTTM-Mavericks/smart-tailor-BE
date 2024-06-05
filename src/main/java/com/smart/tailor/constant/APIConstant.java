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
        public static final String CHECK_VERIFY = "/check-verify";
        public static final String CHECK_VERIFY_PASSWORD = "/check-verify-password";
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
     * Brand API
     */
    public class BrandAPI {
        public static final String BRAND = APIConstant.API + "/brand";
        public static final String GET_BRAND = "/get-brand";
        public static final String ADD_NEW_BRAND = "/add-new-brand";
        public static final String CHECK_VERIFY = "/check-verify";
        public static final String UPLOAD_BRAND_INFOR = "/upload-brand-infor";
        public static final String VERIFY = "/verify";
    }

    /**
     * Role API
     */
    public class RoleAPI {
        public static final String ROLE = APIConstant.API + "/roles";
        public static final String GET_ALL_ROLES = "/get-all-roles";
        public static final String ADD_NEW_ROLES = "/add-new-roles";
    }

    /**
     * Category API
     */
    public class CategoryAPI {
        public static final String CATEGORY = APIConstant.API + "/category";
        public static final String ADD_NEW_CATEGORY = "/add-new-category";
        public static final String GET_ALL_CATEGORY = "/get-all-category";
    }

    /**
     * Material API
     */
    public class MaterialAPI {
        public static final String MATERIAL = APIConstant.API + "/material";
        public static final String ADD_NEW_MATERIAL = "/add-new-material";
        public static final String GET_ALL_MATERIAL = "/get-all-material";
    }

    /**
     * Brand Material API
     */
    public class BrandMaterialAPI {
        public static final String BRAND_MATERIAL = APIConstant.API + "/brand-material";
        public static final String ADD_NEW_BRAND_MATERIAL = "/add-new-brand-material";
        public static final String GET_ALL_BRAND_MATERIAL = "/get-all-brand-material";
        public static final String GET_ALL_BRAND_MATERIAL_BY_BRAND_NAME = "/get-all-brand-material-by-brand-name";
    }
}
