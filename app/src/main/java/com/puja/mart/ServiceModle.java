package com.puja.mart;

import com.google.gson.annotations.SerializedName;

public class ServiceModle {

    @SerializedName("product_type")
    private String productType;

    @SerializedName("product_id")
    private String productId;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("product_mrp")
    private Double productMrp;

    @SerializedName("product_disc")
    private int productDisc;

    @SerializedName("product_sell")
    private Double productSell;

    @SerializedName("product_thumb")
    private String productThumb;

    @SerializedName("id")
    private String id;

    @SerializedName("date")
    private String date;

    @SerializedName("status")
    private String status;

    @SerializedName("product_detail")
    private String product_detail;

    @SerializedName("product_slug")
    private String product_slug;

    public ServiceModle(String productType, String productId, String productName, Double productMrp, int productDisc, Double productSell, String productThumb, String date, String id, String status, String product_detail, String product_slug) {
        this.productType = productType;
        this.productId = productId;
        this.productName = productName;
        this.productMrp = productMrp;
        this.productDisc = productDisc;
        this.productSell = productSell;
        this.productThumb = productThumb;
        this.date = date;
        this.id = id;
        this.status = status;
        this.product_detail = product_detail;
        this.product_slug = product_slug;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProduct_detail() {
        return product_detail;
    }

    public void setProduct_detail(String product_detail) {
        this.product_detail = product_detail;
    }

    public String getProduct_slug() {
        return product_slug;
    }

    public void setProduct_slug(String product_slug) {
        this.product_slug = product_slug;
    }

    // Default constructor
    public ServiceModle() {
    }

    // Parameterized constructor
    public ServiceModle(String productType, String productId, String productName, Double productMrp, int productDisc, Double productSell, String productThumb) {
        this.productType = productType;
        this.productId = productId;
        this.productName = productName;
        this.productMrp = productMrp;
        this.productDisc = productDisc;
        this.productSell = productSell;
        this.productThumb = productThumb;
    }

    // Getters and setters
    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getProductMrp() {
        return productMrp;
    }

    public void setProductMrp(Double productMrp) {
        this.productMrp = productMrp;
    }

    public int getProductDisc() {
        return productDisc;
    }

    public void setProductDisc(int productDisc) {
        this.productDisc = productDisc;
    }

    public Double getProductSell() {
        return productSell;
    }

    public void setProductSell(Double productSell) {
        this.productSell = productSell;
    }

    public String getProductThumb() {
        return productThumb;
    }

    public void setProductThumb(String productThumb) {
        this.productThumb = productThumb;
    }
}
