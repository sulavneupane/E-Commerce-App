package com.nepalicoders.nepbayapp.objects;

import java.io.Serializable;

/**
 * Created by sulav on 12/9/15.
 */
public class Product implements Serializable {

    String id;
    String imageId;
    String catId;
    String name;
    String productCode;
    String descriptionShort;
    String descriptionLong;
    String legend;
    String rank;
    String price;
    String priceNpr;
    String featured;
    String username;
    String shopCategory;
    String active;
    String maxItems;
    String shippingCost;
    String shippingCostInt;
    String manufacturer;
    String downloadLink;
    String weight;
    String moreImages;
    String outOfStock;
    String productCondition;
    String seller;
    String viewedAt;
    String viewedCount;

    String company;

    String originalPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name.replaceAll("(?<!\\\\)\\\\(?!\\\\)", "");
        this.name = name;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getDescriptionShort() {
        return descriptionShort;
    }

    public void setDescriptionShort(String descriptionShort) {
        descriptionShort = descriptionShort.replaceAll("(?<!\\\\)\\\\(?!\\\\)", "");
        this.descriptionShort = descriptionShort;
    }

    public String getDescriptionLong() {
        return descriptionLong;
    }

    public void setDescriptionLong(String descriptionLong) {
        descriptionLong = descriptionLong.replaceAll("(?<!\\\\)\\\\(?!\\\\)", "");
        this.descriptionLong = descriptionLong;
    }

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPriceNpr() {
        return priceNpr;
    }

    public void setPriceNpr(String priceNpr) {
        this.priceNpr = priceNpr;
    }

    public String getFeatured() {
        return featured;
    }

    public void setFeatured(String featured) {
        this.featured = featured;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getShopCategory() {
        return shopCategory;
    }

    public void setShopCategory(String shopCategory) {
        this.shopCategory = shopCategory;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getMaxItems() {
        return maxItems;
    }

    public void setMaxItems(String maxItems) {
        this.maxItems = maxItems;
    }

    public String getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(String shippingCost) {
        this.shippingCost = shippingCost;
    }

    public String getShippingCostInt() {
        return shippingCostInt;
    }

    public void setShippingCostInt(String shippingCostInt) {
        this.shippingCostInt = shippingCostInt;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        manufacturer = manufacturer.replaceAll("(?<!\\\\)\\\\(?!\\\\)", "");
        this.manufacturer = manufacturer;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getMoreImages() {
        return moreImages;
    }

    public void setMoreImages(String moreImages) {
        this.moreImages = moreImages;
    }

    public String getOutOfStock() {
        return outOfStock;
    }

    public void setOutOfStock(String outOfStock) {
        this.outOfStock = outOfStock;
    }

    public String getProductCondition() {
        return productCondition;
    }

    public void setProductCondition(String productCondition) {
        this.productCondition = productCondition;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getViewedAt() {
        return viewedAt;
    }

    public void setViewedAt(String viewedAt) {
        this.viewedAt = viewedAt;
    }

    public String getViewedCount() {
        return viewedCount;
    }

    public void setViewedCount(String viewedCount) {
        this.viewedCount = viewedCount;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        company = company.replaceAll("(?<!\\\\)\\\\(?!\\\\)", "");
        this.company = company;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", imageId='" + imageId + '\'' +
                ", catId='" + catId + '\'' +
                ", name='" + name + '\'' +
                ", productCode='" + productCode + '\'' +
                ", descriptionShort='" + descriptionShort + '\'' +
                ", descriptionLong='" + descriptionLong + '\'' +
                ", legend='" + legend + '\'' +
                ", rank='" + rank + '\'' +
                ", price='" + price + '\'' +
                ", priceNpr='" + priceNpr + '\'' +
                ", featured='" + featured + '\'' +
                ", username='" + username + '\'' +
                ", shopCategory='" + shopCategory + '\'' +
                ", active='" + active + '\'' +
                ", maxItems='" + maxItems + '\'' +
                ", shippingCost='" + shippingCost + '\'' +
                ", shippingCostInt='" + shippingCostInt + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", downloadLink='" + downloadLink + '\'' +
                ", weight='" + weight + '\'' +
                ", moreImages='" + moreImages + '\'' +
                ", outOfStock='" + outOfStock + '\'' +
                ", productCondition='" + productCondition + '\'' +
                ", seller='" + seller + '\'' +
                ", viewedAt='" + viewedAt + '\'' +
                ", viewedCount='" + viewedCount + '\'' +
                ", company='" + company + '\'' +
                ", originalPrice='" + originalPrice + '\'' +
                '}';
    }
}
