package com.smart.tailor.constant;

public class MessageConstant {
    /**
     * ERROR MESSAGE
     */
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL SERVER ERROR";
    public static final String BAD_REQUEST = "BAD REQUEST";
    public static final String MISSING_ARGUMENT = "MISSING ARGUMENT";
    public static final String RESOURCE_NOT_FOUND = "Resource Not Found!";
    public static final String INVALID_DATA_TYPE = "Invalid Data Type";
    public static final String INVALID_EXCEL_FILE_FORMAT = "Invalid Excel File Format";
    public static final String DATA_IS_EMPTY = "Data is empty";
    /**
     * FORMATION ERROR
     */
    public static final String INVALID_EMAIL = "Invalid Email!";
    public static final String INVALID_PASSWORD = "Invalid Password!";
    public static final String INVALID_DATA_TYPE_COLUMN_NEED_TYPE_STRING = "Invalid Data Type Column Need Type String";
    public static final String INVALID_DATA_TYPE_COLUMN_NEED_TYPE_NUMERIC = "Invalid Data Type Column Need Type Numeric";
    public static final String INVALID_NEGATIVE_NUMBER_NEED_POSITIVE_NUMBER = "Invalid Negative Number Need Positive Number";
    public static final String ERROR_READING_EXCEL_FILE = "Error Reading Excel File";
    /**
     * SUCCESS MESSAGE
     */
    public static final String GET_DATA_FROM_EXCEL_SUCCESS = "Get Data From Excel Success";
    /**
     * ROLE
     */
    public static final String GET_ALL_ROLES_SUCCESSFULLY = "Get All Roles Successfully!";
    public static final String ROLE_LIST_IS_EMPTY = "Role List Is Empty!";
    public static final String CAN_NOT_FIND_ROLE = "Can Not Find Role";

    /**
     * BRAND
     */
    public static final String CAN_NOT_FIND_BRAND = "Can Not Find Brand";
    public static final String GET_BRAND_SUCCESSFULLY = "Get Brand Successfully!";
    public static final String REGISTER_NEW_BRAND_FAILED = "Failed To Register New Brand!";
    public static final String REGISTER_NEW_BRAND_SUCCESSFULLY = "Register New Brand Successfully!";


    /**
     * AUTHENTICATION
     */
    /*    SUCCESS     */
    public static final String REGISTER_NEW_USER_SUCCESSFULLY = "Register New User Successfully!";
    public static final String SEND_MAIL_FOR_VERIFY_ACCOUNT_SUCCESSFULLY = "Check Email To Verify Account!";
    public static final String SEND_MAIL_FOR_UPDATE_PASSWORD_SUCCESSFULLY = "Check Email To Update Password!";
    public static final String UPDATE_PASSWORD_SUCCESSFULLY = "Update Password Successfully!";
    public static final String LOGIN_SUCCESSFULLY = "Login Successfully!";
    public static final String ACCOUNT_VERIFIED_SUCCESSFULLY = "Account Verified Successfully!";
    public static final String ACCOUNT_IS_VERIFIED = "Account Is Verified!";
    public static final String ACCOUNT_NOT_VERIFIED = "Account Is Not Verified!";
    public static final String REFRESH_TOKEN_SUCCESSFULLY = "Refresh Token Successfully!";
    public static final String RESEND_MAIL_NEW_TOKEN_SUCCESSFULLY = "Resend Mail New Token Successfully";

    /*    FAIL     */
    public static final String DUPLICATE_REGISTERED_EMAIL = "Duplicate Registered Email!";
    public static final String INVALID_VERIFICATION_TOKEN = "Invalid Verification Token!";
    public static final String INVALID_EMAIL_OR_PASSWORD = "Invalid Email Or Password!";
    public static final String EMAIL_IS_NOT_EXISTED = "Email is not existed!";
    public static final String PHONE_NUMBER_IS_EXISTED = "Phone Number is existed!";
    public static final String UPDATE_PASSWORD_FAILED = "Failed To Update Password!";
    public static final String REGISTER_NEW_USER_FAILED = "Failed To Register New User!";
    public static final String REFRESH_TOKEN_FAILED = "Failed To Refresh Token!";
    public static final String TOKEN_ALREADY_EXPIRED = "Token already expired!";
    public static final String TOKEN_IS_VALID = "Token is Valid!";

    /**
     * CUSTOMER
     */
    /*    SUCCESS     */
    public static final String UPDATE_PROFILE_CUSTOMER_SUCCESSFULLY = "Update Profile Customer Successfully!";

    /*    FAIL     */
    public static final String CUSTOMER_NOT_FOUND = "Customer not found!";
    public static final String UPDATE_PROFILE_CUSTOMER_FAIL = "Update Profile Customer Fail!";

    /**
     * MATERIAL
     */
    /*    SUCCESS     */
    public static final String GET_ALL_MATERIAL_SUCCESSFULLY = "Get All Material Successfully!";
    public static final String CAN_NOT_FIND_ANY_MATERIAL = "Can Not Find Any Material!";
    public static final String ADD_NEW_MATERIAL_SUCCESSFULLY = "Add New Material Successfully!";
    public static final String ADD_NEW_CATEGORY_AND_MATERIAL_BY_EXCEL_FILE_SUCCESSFULLY = "Add New Category And Material By Excel File Successfully!";
    public static final String EXPORT_CATEGORY_AND_MATERIAL_BY_EXCEL_FILE_SUCCESSFULLY = "Export Category And Material By Excel File Successfully!";
    public static final String UPDATE_MATERIAL_SUCCESSFULLY = "Update Material Successfully";
    public static final String GET_MATERIAL_BY_ID_SUCCESSFULLY = "Get Material By ID Successfully";
    public static final String CHANGE_STATUS_MATERIAL_SUCCESSFULLY = "Change Status Material Successfully";
    public static final String GENERATE_SAMPLE_CATEGORY_MATERIAL_SUCCESSFULLY = "Generate Sample Category Material Successfully!";

    /*    FAIL     */
    public static final String ADD_NEW_MATERIAL_FAIL = "Add New Material Fail!";
    public static final String MATERIAL_IS_EXISTED = "Material is Existed";
    public static final String WRONG_TYPE_OF_CATEGORY_AND_MATERIAL_EXCEL_FILE = "Wrong Type Of Category And Material Excel File!";
    public static final String DUPLICATE_CATEGORY_AND_MATERIAL_IN_EXCEL_FILE = "Duplicate Category And Material in Excel File!";
    public static final String ADD_NEW_CATEGORY_AND_MATERIAL_BY_EXCEL_FILE_FAIL = "Add New Category And Material By Excel File Fail!";
    public static final String CATEGORY_AND_MATERIAL_EXCEL_FILE_HAS_EMPTY_DATA = "Category and Material Excel File has Empty Data";
    public static final String CATEGORY_AND_MATERIAL_IS_NOT_EXISTED = "Material is not Existed";

    /**
     * BRAND MATERIAL
     */
    /*    SUCCESS     */
    public static final String GET_ALL_BRAND_MATERIAL_SUCCESSFULLY = "Get All Brand Material Successfully!";
    public static final String CAN_NOT_FIND_ANY_BRAND_MATERIAL = "Can Not Find Any Brand Material!";
    public static final String ADD_NEW_BRAND_MATERIAL_SUCCESSFULLY = "Add New Brand Material Successfully!";
    public static final String ADD_NEW_BRAND_MATERIAL_BY_EXCEL_FILE_SUCCESSFULLY = "Add New Brand Material By Excel File Successfully!";
    public static final String UPDATE_BRAND_MATERIAL_SUCCESSFULLY = "Update Brand Material Successfully!";

    /*    FAIL     */
    public static final String ADD_NEW_BRAND_MATERIAL_FAIL = "Add New Brand Material Fail!";
    public static final String ADD_NEW_BRAND_MATERIAL_BY_EXCEL_FILE_FAIL = "Add New Brand Material By Excel File Fail!";
    public static final String BRAND_MATERIAL_IS_EXISTED = "Brand Material is Existed";
    public static final String BRAND_PRICE_MUST_BE_BETWEEN_BASE_PRICE_MULTIPLE_WITH_PERCENTAGE_FLUCTUATION = "Brand Price Must be Between Base Price Multiple With Percentage Fluctuation";
    public static final String BRAND_MATERIAL_EXCEL_FILE_HAS_EMPTY_DATA = "Brand Material Excel File has Empty Data";
    public static final String DUPLICATE_BRAND_MATERIAL_IN_EXCEL_FILE = "Duplicate Brand Material in Excel File!";
    public static final String WRONG_TYPE_OF_BRAND_MATERIAL_EXCEL_FILE = "Wrong Type Of Brand Material Excel File!";
    /**
     * EXPERT TAILORING
     */
    /*    SUCCESS     */
    public static final String ADD_NEW_EXPERT_TAILORING_SUCCESSFULLY = "Add New Expert Tailoring Successfully!";
    public static final String GET_EXPERT_TAILORING_BY_NAME_SUCCESSFULLY = "Get Expert Tailoring By Name Successfully!";
    public static final String ADD_NEW_EXPERT_TAILORING_BY_EXCEL_FILE_SUCCESSFULLY = "Add New Expert Tailoring By Excel File Successfully!";
    public static final String GET_ALL_EXPERT_TAILORING_SUCCESSFULLY = "Get All Expert Tailoring Successfully!";
    public static final String GENERATE_SAMPLE_EXPERT_TAILORING_SUCCESSFULLY = "Generate Sample Expert Tailoring Successfully!";
    public static final String UPDATE_EXPERT_TAILORING_SUCCESSFULLY = "Update Expert Tailoring Successfully";
    public static final String GET_EXPERT_TAILORING_BY_ID_SUCCESSFULLY = "Get Expert Tailoring By ID Successfully";
    public static final String CHANGE_STATUS_EXPERT_TAILORING_SUCCESSFULLY = "Change Status Expert Tailoring Successfully";
    /*    FAIL     */
    public static final String ADD_NEW_EXPERT_TAILORING_FAIL = "Add New Expert Tailoring Fail!";
    public static final String CAN_NOT_FIND_ANY_EXPERT_TAILORING = "Can Not Find Any Expert Tailoring Material!";
    public static final String ADD_NEW_EXPERT_TAILORING_BY_EXCEL_FILE_FAIL = "Add New Expert Tailoring By Excel File Fail!";
    public static final String EXPERT_TAILORING_IS_EXISTED = "Expert Tailoring is Existed";
    public static final String EXPERT_TAILORING_NAME_IS_EXISTED = "Expert Tailoring Name is Existed";
    public static final String DUPLICATE_EXPERT_TAILORING_DATA = "Duplicate Expert Tailoring Data";
    public static final String DUPLICATE_EXPERT_TAILORING_IN_EXCEL_FILE = "Duplicate Expert Tailoring in Excel File!";
    public static final String WRONG_TYPE_OF_EXPERT_TAILORING_EXCEL_FILE = "Wrong Type Of Expert Tailoring Excel File!";
    public static final String EXPERT_TAILORING_EXCEL_FILE_HAS_EMPTY_DATA = "Expert Tailoring Excel File has Empty Data";
    /**
     * NOTIFICATION
     */
    public static final String SEND_NOTIFICATION_SUCCESSFULLY = "Notification sent successfully!";
    public static final String SEND_NOTIFICATION_FAIL = "Fail To Send Notification!";

    /**
     * CATEGORY
     */
    /*    SUCCESS  */
    public static final String GET_ALL_CATEGORY_SUCCESSFULLY = "Get All Category Successfully!";
    public static final String GET_CATEGORY_BY_ID_SUCCESSFULLY = "Get Category By ID Successfully!";
    public static final String ADD_NEW_CATEGORY_SUCCESSFULLY = "Add New Category Successfully!";
    public static final String UPDATE_CATEGORY_SUCCESSFULLY = "Update Category Successfully!";
    /*    FAIL     */
    public static final String CAN_NOT_FIND_ANY_CATEGORY = "Can Not Find Any Category!";
    public static final String ADD_NEW_CATEGORY_FAIL = "Add New Category Fail!";
    public static final String CATEGORY_IS_EXISTED = "Category is Existed";
    public static final String CATEGORY_NAME_IS_EXISTED = "Category Name is Existed";
}
