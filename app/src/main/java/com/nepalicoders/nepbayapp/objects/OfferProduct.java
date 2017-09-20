package com.nepalicoders.nepbayapp.objects;

import java.io.Serializable;

/**
 * Created by sulav on 12/14/15.
 */
public class OfferProduct implements Serializable {

    String id;
    String type;
    String productId;
    String imageId;
    String itemId;
    String productName;
    String originalPrice;
    String offerType;
    String discountFlat;
    String discountPercent;
    String discountItemId;
    String price;
    String username;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public String getDiscountFlat() {
        return discountFlat;
    }

    public void setDiscountFlat(String discountFlat) {
        this.discountFlat = discountFlat;
    }

    public String getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(String discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getDiscountItemId() {
        return discountItemId;
    }

    public void setDiscountItemId(String discountItemId) {
        this.discountItemId = discountItemId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "OfferProduct{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", productId='" + productId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", productName='" + productName + '\'' +
                ", originalPrice='" + originalPrice + '\'' +
                ", offerType='" + offerType + '\'' +
                ", discountFlat='" + discountFlat + '\'' +
                ", discountPercent='" + discountPercent + '\'' +
                ", discountItemId='" + discountItemId + '\'' +
                ", price='" + price + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
