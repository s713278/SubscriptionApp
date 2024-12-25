package com.app.config;


public class AppConstants {

    // Regular expression to validate a mobile number (Indian mobile number format example)
    public static final String MOBILE_REGEX = "^\\d{10}$";
    // Updated regular expression to validate an email format, preventing consecutive dots anywhere in the domain
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z]{2,}$";

    public static final String PAGE_NUMBER = "0";
    public static final String PAGE_SIZE = "2";
    public static final String SORT_CATEGORIES_BY = "categoryId";
    public static final String SORT_STORE_BY = "businessName";
    public static final String SORT_PRODUCTS_BY = "productId";
    public static final String SORT_USERS_BY = "userId";
    public static final String SORT_ORDERS_BY = "totalAmount";
    public static final String SORT_DIR = "asc";
    public static final Long ADMIN_ROLE_ID = 101L;
    public static final Long USER_ROLE_ID = 102L;
    public static final Long VENDOR_ROLE_ID = 103L;
    public static final Long CC_ROLE_ID = 104L;
    public static final Long VENDOR_STAFF_ROLE_ID = 105L;
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    public static final String[] PUBLIC_POST_URLS = { "/v1/auth/**",
            "/v1/vendor/*/subscription",
            "/v1/vendor/*/subscription/*","/v1/vendor/*/customer/*"};
    
    public static final String[] PUBLIC_GET_URLS = { "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.**",
            "/api/store/*/categories", "/api/store/*/categories/*/products",
            "/api/store/*/products", "/api/stores/*" ,
            "/vendor/*/subscription/*","/vendor/*/customer/*","/v1/vendors/*","/v1/vendors/*/products","/custom-api-docs/**"};
    
    public static final String[] USER_PUT_URLS = {"/v1/users/*" };
    public static final String[] USER_PATCH_URLS = {"/v1/users/*" };
    public static final String[] USER_GET_URLS = {"/v1/auth/profile","/v1/users/*","/v1/users/*/vendor/*" };

    public static final String[] VENDOR_USER_SUB_POST_URLS = {"/v1/vendors/*/users/*/subs" };
    public static final String[] VENDOR_USER_SUB_GET_URLS = {"/v1/vendors/*/users/*/subs/*" };
    public static final String[] VENDOR_USER_SUB_DELETE_URLS = {"/v1/vendors/*/users/*/subs/*" };
    public static final String[] VENDOR_USER_SUB_PATCH_URLS = {"/v1/vendors/*/users/*/subs/*" ,"/v1/vendors/*/users/*/subs/*/status" };

    public static final String[] ADMIN_URLS = { "/api/store/*/admin/**" };
    public static final String[] VENDOR_URLS = { "/api/store/*" };
    
    public static final String SIGN_UP_URL="/v1/auth/signup/**";
    public static final String SIGN_IN_URL="/v1/auth/signin";
    public static final String SIGN_OUT_URL="/v1/auth/signout";
    public static final String JSESSION_ID = "JSESSIONID";
    public static final String AUTHORIZATION_HEADER="Authorization";
    public static final String SECURITY_CONTEXT_PARAM="E-Commerce Application";

    public static final String SMS_FAST2SMS_PROVIDER="fast2sms";
    public static final String SMS_VONAGE_PROVIDER="vonage";
    public static final int SUB_MAX_ALLOWED_START_DATE_DAYS =15;
    public static final int SUB_MAX_ALLOWED_END_DATE_DAYS =365;
    public static final String SUB_ONE_TIME_DELIVERY_DAYS="delivery_days";
    public static final String SUB_ONE_TIME_SUB_TYPE="type";
}
