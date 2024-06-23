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
        public static final String CHECK_VERIFY_ACCOUNT = "/check-verify-account";
        public static final String CHECK_VERIFY_FORGOT_PASSWORD = "/check-verify-forgot-password";
        public static final String CHECK_VERIFY_CHANGE_PASSWORD = "/check-verify-change-password";
        public static final String VERIFY_PASSWORD = "/verify-password";
        public static final String CHANGE_PASSWORD = "/change-password";
        public static final String FORGOT_PASSWORD = "/forgot-password";
        public static final String UPDATE_PASSWORD = "/update-password";
        public static final String GOOGLE_REGISTER = "/google-register";
        public static final String FACEBOOK_REGISTER = "/facebook-register";
        public static final String LOGIN = "/login";
        public static final String GOOGLE_LOGIN = "/google-login";
        public static final String FACEBOOK_LOGIN = "/facebook-login";
        public static final String LOG_OUT = "/log-out";
        public static final String REFRESH_TOKEN = "/refresh-token";
        public static final String RESEND_VERIFICATION_TOKEN = "/resend-verification-token";
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
        public static final String GET_BRAND_REGISTRATION_PAYMENT = "/get-brand-registration-payment";
        public static final String ADD_EXPERT_TAILORING_FOR_BRAND = "/add-expert-tailoring-for-brand";
        public static final String VERIFY = "/verify";
        public static final String ACCEPT_BRAND = "/accept-brand";
        public static final String REJECT_BRAND = "/reject-brand";
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
        public static final String GET_CATEGORY_BY_ID = "/get-category-by-id";
        public static final String UPDATE_CATEGORY = "/update-category";
    }

    /**
     * Material API
     */
    public class MaterialAPI {
        public static final String MATERIAL = APIConstant.API + "/material";
        public static final String ADD_NEW_MATERIAL = "/add-new-material";
        public static final String GET_ALL_MATERIAL = "/get-all-material";
        public static final String GET_ALL_ACTIVE_MATERIAL = "/get-all-active-material";
        public static final String GET_MATERIAL_BY_ID = "/get-material-by-id";
        public static final String ADD_NEW_CATEGORY_MATERIAL_BY_EXCEL_FILE = "/add-new-category-material-by-excel-file";
        public static final String EXPORT_CATEGORY_MATERIAL_FOR_BRAND_BY_EXCEL = "/export-category-material-for-brand-by-excel";
        public static final String UPDATE_MATERIAL = "/update-material";
        public static final String UPDATE_STATUS_MATERIAL = "update-status-material";
        public static final String GENERATE_SAMPLE_CATEGORY_MATERIAL_BY_EXCEL_FILE = "/generate-sample-category-material-by-excel-file";
    }

    /**
     * Brand Material API
     */
    public class BrandMaterialAPI {
        public static final String BRAND_MATERIAL = APIConstant.API + "/brand-material";
        public static final String ADD_NEW_BRAND_MATERIAL = "/add-new-brand-material";
        public static final String GET_ALL_BRAND_MATERIAL = "/get-all-brand-material";
        public static final String GET_ALL_BRAND_MATERIAL_BY_BRAND_NAME = "/get-all-brand-material-by-brand-name";
        public static final String ADD_NEW_BRAND_MATERIAL_BY_EXCEL_FILE = "/add-new-brand-material-by-excel-file";
        public static final String UPDATE_BRAND_MATERIAL = "/update-brand-material";
    }

    /**
     * Customer API
     */
    public class CustomerAPI {
        public static final String CUSTOMER = APIConstant.API + "/customer";
        public static final String UPDATE_CUSTOMER_PROFILE = "/update-customer-profile";
    }

    /**
     * Customer API
     */
    public class ExpertTailoringAPI {
        public static final String EXPERT_TAILORING = APIConstant.API + "/expert-tailoring";
        public static final String ADD_NEW_EXPERT_TAILORING = "/add-new-expert-tailoring";
        public static final String GET_ALL_EXPERT_TAILORING = "/get-all-expert-tailoring";
        public static final String GET_ALL_EXPERT_TAILORING_BY_EXPERT_TAILORING_NAME = "/get-expert-tailoring-by-name";
        public static final String ADD_NEW_EXPERT_TAILORING_BY_EXCEL_FILE = "/add-new-expert-tailoring-by-excel-file";
        public static final String GET_ALL_EXPERT_TAILORING_BY_EXCEL_FILE = "/get-all-expert-tailoring-by-excel-file";
        public static final String GENERATE_SAMPLE_EXPERT_TAILORING_BY_EXCEL_FILE = "/generate-sample-expert-tailoring-by-excel-file";
        public static final String GET_EXPERT_TAILORING_BY_ID = "/get-expert-tailoring-by-id";
        public static final String UPDATE_EXPERT_TAILORING = "/update-expert-tailoring";
        public static final String UPDATE_STATUS_EXPERT_TAILORING = "/update-status-expert-tailoring";

    }

    /**
     * Notification API
     */
    public class NotificationAPI {
        public static final String Notification = APIConstant.API + "/notification";
        public static final String SEND_PUBLIC_NOTIFICATION = "/send-public-notification";
        public static final String SEND_NOTIFICATION = "/send-notification";
    }

    /**
     * Product API
     */
    public class ProductAPI {
        public static final String Product = APIConstant.API + "/product";
        public static final String GET_PRODUCT = "/get-product";
        public static final String GET_ALL_PRODUCT_BY_BRAND_NAME = "/get-all-product-by-brand-name";
        public static final String GET_ALL_PRODUCT_BY_BRAND_ID = "/get-all-product-by-brand-id";
        public static final String GET_ALL_PRODUCT_BY_DESIGN_ID = "/get-all-product-by-design-id";
        public static final String GET_ALL_PRODUCT_BY_USER_ID = "/get-all-product-by-user-id";
        public static final String ADD_NEW_PRODUCT = "/add-new-product";
    }

    /**
     * Design API
     */
    public class DesignAPI {
        public static final String DESIGN = APIConstant.API + "/design";
        public static final String ADD_NEW_DESIGN = "/add-new-design";
        public static final String GET_DESIGN_BY_ID = "/get-design-by-id";
        public static final String GET_ALL_DESIGN_BY_USER_ID = "/get-all-design-by-user-id";
        public static final String GET_ALL_DESIGN = "/get-all-design";
        public static final String GET_ALL_DESIGN_BY_CUSTOMER_ID = "/get-all-design-by-customer-id";
        public static final String GET_ALL_DESIGN_BY_BRAND_ID = "/get-all-design-by-brand-id";
        public static final String UPDATE_PUBLIC_STATUS_BY_DESIGN_ID = "/update-public-status-design-id";
    }

    /**
     * PartOfDesign API
     */
    public class PartOfDesignAPI {
        public static final String PART_OF_DESIGN = APIConstant.API + "/part-of-design";
        public static final String GET_PART_OF_DESIGN_BY_ID = "/get-part-of-design-by-id";
        public static final String GET_ALL_PART_OF_DESIGN_BY_DESIGN_ID = "/get-all-part-of-design-by-design-id";
        public static final String GET_ALL_PART_OF_DESIGN = "/get-all-part-of-design";
    }

    /**
     * ItemMask API
     */
    public class ItemMaskAPI {
        public static final String ITEM_MASK = APIConstant.API + "/item-mask";
        public static final String GET_ITEM_MASK_BY_ID = "/get-item-mask-by-id";
        public static final String GET_ALL_ITEM_MASK_BY_PART_OF_DESIGN_ID = "/get-all-item-mask-by-part-of-design-id";
        public static final String GET_ALL_ITEM_MASK = "/get-all-item-mask";
    }
}
