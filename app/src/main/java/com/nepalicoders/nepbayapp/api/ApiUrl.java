package com.nepalicoders.nepbayapp.api;

/**
 * Created by Sabin on 12/7/2015.
 */
public class ApiUrl {

    public static final String baseUrl = "http://developers.nepbay.com/api/index.php"; //This is the base url


    public static final String CategoryListUrl=baseUrl+"/welcome_categories/display";
    public static final String subCategoryListUrl=baseUrl+"/welcome_categories/display_inside_menu";
    public static final String featuredCategoryUrl=baseUrl+"/FeaturedCategory/display_featured_items";
    public static final String productList=baseUrl+"/welcome_categories/display_products";

    public static final String NEPBAY_BASE_URL = "http://www.nepbay.com";

    public static final String BASE_URL = "http://developers.nepbay.com/api";
    public static final String SEARCH_PRODUCT_URL = BASE_URL + "/search/search_product";
    public static final String USER_LOGIN_URL = BASE_URL + "/user/login";
    public static final String USER_REGISTER_URL = BASE_URL + "/user/register";
    public static final String USER_PROFILE_URL = BASE_URL + "/user/user_profile";
    public static final String USER_LOG_OUT_URL = BASE_URL + "/user/logout";
    public static final String USER_PROFILE_UPDATE_URL = BASE_URL + "/user/update_user_profile";
    public static final String USER_PROFILE_CHANGE_PASSWORD_URL = BASE_URL + "/user/change_user_password";
    public static final String VIEW_ORDERS_URL = BASE_URL + "/order/view_order";
    public static final String HOME_BANNER_URL = BASE_URL + "/banner/show_header_banners";
    public static final String HOME_BANNER_IMAGE_BASE_URL = NEPBAY_BASE_URL + "/images/";

    public static final String PLACE_ORDER_URL = BASE_URL + "/order/save_order";

    public static final String SHOP_PHONE_URL = BASE_URL + "/search/shop_phone_number";

    public static final String NEPBAY_PRODUCT_IMAGES_URL_TEMPORARY = "http://www.nepbay.com/image.php?id=";
    public static final String PRODUCTS_DISPLAY_URL_TEMPORARY = BASE_URL + "/Welcome_categories/display_products";
    public static final String OFFERS_DISPLAY_URL_TEMPORARY = BASE_URL + "/offer/show_offer";

    public static final String USER_ACTIVATION_URL = BASE_URL + "/user/activate";

    public static final String GENERATE_RRODUCT_URL = BASE_URL + "/welcome_categories/get_product_url";

    public static final String GENERATE_SHOP_URL = BASE_URL + "/welcome_categories/get_shop_url";

    public static final String GET_RRODUCT_URL = BASE_URL + "/welcome_categories/get_product";

}
