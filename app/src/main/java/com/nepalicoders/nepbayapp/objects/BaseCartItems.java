package com.nepalicoders.nepbayapp.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sabin on 12/3/15.
 */

public class BaseCartItems {

    private int totalProducts =0, totalQuantity=0;
    private double totalPrice =0;

    public void reset(){
        totalProducts = 0;
        totalQuantity = 0;
        totalPrice = 0;
    }

    private List<CartItems> items = new ArrayList<>();

    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity += totalQuantity;
    }

    public void setTotalNewQuantity(int totalNewQuantity){
        this.totalQuantity = totalNewQuantity;
    }

    public void setTotalNewPrice(double totalNewPrice){
        this.totalPrice = totalNewPrice;
    }


    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice += totalPrice;
    }

    public List<CartItems> getItems() {
        return items;
    }

    public void setItems(List<CartItems> items) {
        this.items = items;
    }

    public static class CartItems {

        String name, sellerName, productImage;
        double Price;
        int quantity;
        long id;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSellerName() {
            return sellerName;
        }

        public void setSellerName(String sellerName) {
            this.sellerName = sellerName;
        }

        public double getPrice() {
            return Price;
        }

        public void setPrice(double price) {
            Price = price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getProductImage() {
            return productImage;
        }

        public void setProductImage(String productImage) {
            this.productImage = productImage;
        }

        @Override
        public String toString() {
            return "CartItems{" +
                    "name='" + name + '\'' +
                    ", sellerName='" + sellerName + '\'' +
                    ", productImage='" + productImage + '\'' +
                    ", Price=" + Price +
                    ", quantity=" + quantity +
                    ", id=" + id +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "BaseCartItems{" +
                "totalProducts=" + totalProducts +
                ", totalQuantity=" + totalQuantity +
                ", totalPrice=" + totalPrice +
                ", items=" + items +
                '}';
    }
}