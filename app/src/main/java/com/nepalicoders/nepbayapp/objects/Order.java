package com.nepalicoders.nepbayapp.objects;

/**
 * Created by sulav on 12/10/15.
 */
public class Order {

    String buyerId;
    String userId;
    String itemName;
    String itemCost;
    String itemQuantity;
    String TotalCost;
    String date;
    String orderTotal;
    String orderNumber;
    String shipping;
    String username;
    String status;
    String validator;
    String reason;
    String commissionStatus;
    String affiliatePaymentId;
    String username_;
    String commission;
    String handlingFee;
    String shippingFee;
    String byTelephone;
    String telephoneOrderId;
    String assignStatus;

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getItemCost() {
        return itemCost;
    }

    public void setItemCost(String itemCost) {
        this.itemCost = itemCost;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity= itemQuantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getTotalCost() {
        return TotalCost;
    }

    public void setTotalCost(String TotalCost) {
        this.TotalCost= TotalCost;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getValidator() {
        return validator;
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCommissionStatus() {
        return commissionStatus;
    }

    public void setCommissionStatus(String commissionStatus) {
        this.commissionStatus = commissionStatus;
    }

    public String getAffiliatePaymentId() {
        return affiliatePaymentId;
    }

    public void setAffiliatePaymentId(String affiliatePaymentId) {
        this.affiliatePaymentId = affiliatePaymentId;
    }

    public String getUsername_() {
        return username_;
    }

    public void setUsername_(String username_) {
        this.username_ = username_;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getHandlingFee() {
        return handlingFee;
    }

    public void setHandlingFee(String handlingFee) {
        this.handlingFee = handlingFee;
    }

    public String getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(String shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getByTelephone() {
        return byTelephone;
    }

    public void setByTelephone(String byTelephone) {
        this.byTelephone = byTelephone;
    }

    public String getTelephoneOrderId() {
        return telephoneOrderId;
    }

    public void setTelephoneOrderId(String telephoneOrderId) {
        this.telephoneOrderId = telephoneOrderId;
    }

    public String getAssignStatus() {
        return assignStatus;
    }

    public void setAssignStatus(String assignStatus) {
        this.assignStatus = assignStatus;
    }

    @Override
    public String toString() {
        return orderNumber;
    }
}
