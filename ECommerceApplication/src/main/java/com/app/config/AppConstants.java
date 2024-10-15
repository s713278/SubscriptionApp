package com.app.config;

public class AppConstants {

    public static final String PAGE_NUMBER = "0";
    public static final String PAGE_SIZE = "2";
    public static final String SORT_CATEGORIES_BY = "categoryId";
    public static final String SORT_STORE_BY = "name";
    public static final String SORT_PRODUCTS_BY = "productId";
    public static final String SORT_USERS_BY = "userId";
    public static final String SORT_ORDERS_BY = "totalAmount";
    public static final String SORT_DIR = "asc";
    public static final Long ADMIN_ROLE_ID = 101L;
    public static final Long USER_ROLE_ID = 102L;
    public static final Long VENDOR_ROLE_ID = 103L;
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    public static final String[] PUBLIC_POST_URLS = { "/auth/signup/**" ,"/auth/signin","/auth/refresh",
            "/vendor/*/subscription",
            "/vendor/*/subscription/*","/vendor/*/customer/*"}; 
    
    public static final String[] PUBLIC_GET_URLS = { "/v3/api-docs/**", "/swagger-ui/**", 
            "/api/store/*/categories", "/api/store/*/categories/*/products", 
            "/api/store/*/products", "/api/stores/*" ,
             "/vendor/*/subscription/*","/vendor/*/customer/*"};
    
    public static final String[] USER_PUT_URLS = {"/users/*" };
    public static final String[] USER_PATCH_URLS = {"/users/*" };
    public static final String[] USER_GET_URLS = {"/auth/profile","/users/*","/users/*/vendor/*" };
    
    
    public static final String[] ADMIN_URLS = { "/api/store/*/admin/**" };

    public static final String[] VENDOR_URLS = { "/api/store/*" };
    
    public static final String SIGN_UP_URL="/auth/signup/**";
    public static final String SIGN_IN_URL="/auth/signin";
    public static final String SIGN_OUT_URL="/auth/signout";
    public static final String JSESSION_ID = "JSESSIONID";
    public static final String AUTHORIZATION_HEADER="Authorization";
    public static final String SECURITY_CONTEXT_PARAM="E-Commerce Application";
    
}
