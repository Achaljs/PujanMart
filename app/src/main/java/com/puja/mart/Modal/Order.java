package com.puja.mart.Modal;

import com.google.gson.annotations.SerializedName;

import kotlin.jvm.Synchronized;

public class Order {

    private int id;
    private String orderId;
    private String productId;
    private String price;
    private String disc;
    private String sellPrice;
    private String quantity;
    private String amount;

    @SerializedName("payment_status")
    private String paymentStatus;

    @SerializedName("delivery_status")
    private String deliveryStatus;
    private String dispatchedDate;
    private String deliveryBy;

    private String deliveryDate;


    @SerializedName("product_thumb")
    private String productThumb;

    @SerializedName("product_name")
    private String productName;

    // Constructor
    public Order(int id, String orderId, String productId, String price, String disc, String sellPrice,
                 String quantity, String amount, String paymentStatus, String deliveryStatus,
                 String dispatchedDate, String deliveryBy, String deliveryDate, String productThumb, String productName) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.price = price;
        this.disc = disc;
        this.sellPrice = sellPrice;
        this.quantity = quantity;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.deliveryStatus = deliveryStatus;
        this.dispatchedDate = dispatchedDate;
        this.deliveryBy = deliveryBy;
        this.deliveryDate = deliveryDate;
        this.productThumb = productThumb;
        this.productName = productName;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getDispatchedDate() {
        return dispatchedDate;
    }

    public void setDispatchedDate(String dispatchedDate) {
        this.dispatchedDate = dispatchedDate;
    }

    public String getDeliveryBy() {
        return deliveryBy;
    }

    public void setDeliveryBy(String deliveryBy) {
        this.deliveryBy = deliveryBy;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getProductThumb() {
        return productThumb;
    }

    public void setProductThumb(String productThumb) {
        this.productThumb = productThumb;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}

