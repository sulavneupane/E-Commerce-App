package com.nepalicoders.nepbayapp.json;

import android.util.Log;

import com.nepalicoders.nepbayapp.objects.HomeBanner;
import com.nepalicoders.nepbayapp.objects.OfferProduct;
import com.nepalicoders.nepbayapp.objects.Order;
import com.nepalicoders.nepbayapp.objects.Product;
import com.nepalicoders.nepbayapp.objects.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sulav on 12/9/15.
 */
public class JsonParser {

    public static List<Product> parseSearchResult(String result){
        List<Product> products = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(result);
            JSONArray array = new JSONArray(object.getString("message1"));
            for(int i=0; i<array.length(); i++){
                JSONObject currentItem = new JSONObject(array.getString(i));
                Product product = new Product();
                product.setId(currentItem.getString("id"));
                product.setImageId(currentItem.getString("image_id"));
                product.setCatId(currentItem.getString("cat_id"));
                product.setName(currentItem.getString("name"));
                product.setProductCode(currentItem.getString("product_code"));
                product.setDescriptionShort(currentItem.getString("description_short"));
                product.setDescriptionLong(currentItem.getString("description_long"));
                product.setLegend(currentItem.getString("legend"));
                product.setRank(currentItem.getString("rank"));
                product.setPrice(currentItem.getString("price"));
                product.setPriceNpr(currentItem.getString("price_npr"));
                product.setFeatured(currentItem.getString("featured"));
                product.setUsername(currentItem.getString("username"));
                product.setShopCategory(currentItem.getString("shop_category"));
                product.setActive(currentItem.getString("active"));
                product.setMaxItems(currentItem.getString("max_items"));
                product.setShippingCost(currentItem.getString("shipping_cost"));
                product.setShippingCostInt(currentItem.getString("shipping_cost_int"));
                product.setManufacturer(currentItem.getString("manufacturer"));
                product.setDownloadLink(currentItem.getString("download_link"));
                product.setWeight(currentItem.getString("weight"));
                product.setMoreImages(currentItem.getString("more_images"));
                product.setOutOfStock(currentItem.getString("out_of_stock"));
                product.setProductCondition(currentItem.getString("product_condition"));
                product.setSeller(currentItem.getString("seller"));
                product.setViewedAt(currentItem.getString("viewed_at"));
                product.setViewedCount(currentItem.getString("viewed_count"));

                product.setCompany(currentItem.getString("company"));

                products.add(product);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return products;
    }

    public static UserInfo parseUserInfo(String result){
        UserInfo userInfo = new UserInfo();

        try {
            JSONObject object = new JSONObject(result);

            userInfo.setTotalOrders(object.getString("total_orders"));
            userInfo.setApprovedOrders(object.getString("approved_orders"));
            userInfo.setRejectedOrders(object.getString("rejected_orders"));
            userInfo.setPendingOrders(object.getString("pending_orders"));

            JSONObject userInformation = object.getJSONObject("user_info");

            userInfo.setId(userInformation.getString("id"));
            userInfo.setUsername(userInformation.getString("UserName"));
            userInfo.setFirstName(userInformation.getString("FirstName"));
            userInfo.setLastName(userInformation.getString("LastName"));
            userInfo.setCountry(userInformation.getString("Country"));
            userInfo.setAddress1(userInformation.getString("Address1"));
            userInfo.setCity(userInformation.getString("City"));
            userInfo.setZip(userInformation.getString("ZIP"));
            userInfo.setState(userInformation.getString("State"));
            userInfo.setAddress2(userInformation.getString("Address2"));
            userInfo.setEmail(userInformation.getString("Email"));
            userInfo.setPhone(userInformation.getString("Phone"));
            userInfo.setFax(userInformation.getString("Fax"));
            userInfo.setCityId(userInformation.getString("City_id"));
            userInfo.setDistrictId(userInformation.getString("District_id"));
            userInfo.setVdc(userInformation.getString("Vdc"));
            userInfo.setWardNo(userInformation.getString("Ward_no"));
            userInfo.setLocality(userInformation.getString("Locality"));
            userInfo.setHouseNo(userInformation.getString("House_no"));
            userInfo.setMobileNo(userInformation.getString("Mobile_no"));


            /*if(!object.getString("shipping_info").equals("null")){
                JSONObject shippingInformation = object.getJSONObject("shipping_info");

                userInfo.setShippingId(shippingInformation.getString("id"));
                userInfo.setShippingUsername(shippingInformation.getString("UserName"));
                userInfo.setShippingFirstName(shippingInformation.getString("FirstName"));
                userInfo.setShippingLastName(shippingInformation.getString("LastName"));
                if(!shippingInformation.getString("Country").equals("")) {
                    userInfo.setShippingCountry(shippingInformation.getString("Country"));
                }
                if(!shippingInformation.getString("State").equals("")) {
                    userInfo.setShippingState(shippingInformation.getString("State"));
                }
                if(!shippingInformation.getString("City").equals("")) {
                    userInfo.setShippingCity(shippingInformation.getString("City"));
                }
                if(!shippingInformation.getString("ZIP").equals("")) {
                    userInfo.setShippingZip(shippingInformation.getString("ZIP"));
                }
                if(!shippingInformation.getString("Address1").equals("")) {
                    userInfo.setShippingAddress1(shippingInformation.getString("Address1"));
                }
                if(!shippingInformation.getString("Address2").equals("")) {
                    userInfo.setShippingAddress2(shippingInformation.getString("Address2"));
                }
                userInfo.setShippingOrderNumber(shippingInformation.getString("OrderNumber"));
                userInfo.setShippingCityId(shippingInformation.getString("City_id"));
                userInfo.setShippingDistrictId(shippingInformation.getString("District_id"));
                userInfo.setShippingVdc(shippingInformation.getString("Vdc"));
                userInfo.setShippingWardNo(shippingInformation.getString("Ward_no"));
                userInfo.setShippingLocality(shippingInformation.getString("Locality"));
                userInfo.setShippingHouseNo(shippingInformation.getString("House_no"));
                userInfo.setShippingMobileNo(shippingInformation.getString("Mobile_no"));
                userInfo.setShippingExtraMobileNo(shippingInformation.getString("ExtraMobileNo"));
            }*/

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userInfo;
    }

    public static List<Order> parseOrders(String result){
        List<Order> orders = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(result);
            JSONArray jsonArray = object.getJSONArray("data");

            for(int i=0; i<jsonArray.length(); i++){
                JSONObject currentOrder = jsonArray.getJSONObject(i);

                Order order = new Order();

                //order.setBuyerId(currentOrder.getString("BuyerID"));
                //order.setUserId(currentOrder.getString("UserID"));
                order.setItemName(currentOrder.getString("ItemName"));
                order.setItemCost(currentOrder.getString("ItemCost"));
                order.setItemQuantity(currentOrder.getString("ItemQuantity"));
                order.setTotalCost(currentOrder.getString("TotalCost"));
                order.setDate(currentOrder.getString("Date"));
                //order.setOrderTotal(currentOrder.getString("OrderTotal"));
               // order.setOrderNumber(currentOrder.getString("OrderNumber"));
                //order.setShipping(currentOrder.getString("Shipping"));
                //order.setUsername(currentOrder.getString("UserName"));
                order.setStatus(currentOrder.getString("status"));
                //order.setValidator(currentOrder.getString("validator"));
                //order.setReason(currentOrder.getString("reason"));
                //order.setCommissionStatus(currentOrder.getString("commission_status"));
                //order.setAffiliatePaymentId(currentOrder.getString("affiliate_payment_id"));
                //order.setUsername_(currentOrder.getString("username_"));
                //order.setCommission(currentOrder.getString("commission"));
                //order.setHandlingFee(currentOrder.getString("handling_fee"));
                //order.setShippingFee(currentOrder.getString("shipping_fee"));
                //order.setByTelephone(currentOrder.getString("by_telephone"));
                //order.setTelephoneOrderId(currentOrder.getString("telephone_order_id"));
                //order.setAssignStatus(currentOrder.getString("assign_status"));

                orders.add(order);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public static List<HomeBanner> parseHomeBanners(String result){
        List<HomeBanner> banners = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(result);

            for(int i=0; i<array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                HomeBanner banner = new HomeBanner();

                banner.setBannerId(object.getString("banner_id"));
                banner.setTitle(object.getString("title"));
                banner.setDescription(object.getString("description"));
                banner.setLink(object.getString("link"));
                banner.setImage(object.getString("image"));

                banners.add(banner);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return banners;
    }

    public static ArrayList<Product> parseCategoryProductResult(String result){
        ArrayList<Product> products = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(result);
            for(int i=0; i<array.length(); i++){
                JSONObject currentItem = new JSONObject(array.getString(i));
                Product product = new Product();

                product.setId(currentItem.getString("id"));
                product.setImageId(currentItem.getString("image_id"));
                product.setCatId(currentItem.getString("cat_id"));
                product.setName(currentItem.getString("name"));
                product.setProductCode(currentItem.getString("product_code"));
                product.setDescriptionShort(currentItem.getString("description_short"));
                product.setDescriptionLong(currentItem.getString("description_long"));
                product.setLegend(currentItem.getString("legend"));
                product.setRank(currentItem.getString("rank"));
                product.setPrice(currentItem.getString("price"));
                product.setPriceNpr(currentItem.getString("price_npr"));
                product.setFeatured(currentItem.getString("featured"));
                product.setUsername(currentItem.getString("username"));
                product.setShopCategory(currentItem.getString("shop_category"));
                product.setActive(currentItem.getString("active"));
                product.setMaxItems(currentItem.getString("max_items"));
                product.setShippingCost(currentItem.getString("shipping_cost"));
                product.setShippingCostInt(currentItem.getString("shipping_cost_int"));
                product.setManufacturer(currentItem.getString("manufacturer"));
                product.setDownloadLink(currentItem.getString("download_link"));
                product.setWeight(currentItem.getString("weight"));
                product.setMoreImages(currentItem.getString("more_images"));
                product.setOutOfStock(currentItem.getString("out_of_stock"));
                product.setProductCondition(currentItem.getString("product_condition"));
                product.setSeller(currentItem.getString("seller"));
                product.setViewedAt(currentItem.getString("viewed_at"));
                product.setViewedCount(currentItem.getString("viewed_count"));

                product.setCompany(currentItem.getString("company"));

                products.add(product);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return products;
    }

    public static List<Product> parseOfferProducts(String result){
        List<Product> products = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(result);
            JSONArray array = object.getJSONArray("data");
            for(int i=0;i<array.length(); i++){
                JSONObject currentItem = array.getJSONObject(i);

                Product product = new Product();

                product.setId(currentItem.getString("product_id"));
                product.setUsername(currentItem.getString("username"));
                product.setProductCode(currentItem.getString("product_code"));
                product.setImageId(currentItem.getString("image_id"));
                product.setName(currentItem.getString("product_name"));
                product.setOriginalPrice(currentItem.getString("original_price"));
                product.setPrice(currentItem.getString("price"));
                product.setUsername(currentItem.getString("username"));
                product.setMoreImages(currentItem.getString("more_images"));
                product.setCompany(currentItem.getString("company"));
                product.setDescriptionShort(currentItem.getString("description_short"));

                products.add(product);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            //Log.d("OfferResult", e.getMessage());
        }

        return products;
    }


}
