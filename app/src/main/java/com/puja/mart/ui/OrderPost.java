package com.puja.mart.ui;

public class OrderPost {
    public String city = "";
    public String coupon_code = "";
    public String locality = "";
    public String orderid = "";
    public String pincode = "";
    public String state = "";
    public String user_address = "";
    public String user_mbl = "";
    public String user_name = "";
    public String userid = "";
    public String value = "";

    public String getOrderid() {
        return this.orderid;
    }

    public void setOrderid(String orderid2) {
        this.orderid = orderid2;
    }

    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String userid2) {
        this.userid = userid2;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value2) {
        this.value = value2;
    }

    public String getCoupon_code() {
        return this.coupon_code;
    }

    public void setCoupon_code(String coupon_code2) {
        this.coupon_code = coupon_code2;
    }

    public String getUser_name() {
        return this.user_name;
    }

    public void setUser_name(String user_name2) {
        this.user_name = user_name2;
    }

    public String getUser_mbl() {
        return this.user_mbl;
    }

    public void setUser_mbl(String user_mbl2) {
        this.user_mbl = user_mbl2;
    }

    public String getUser_address() {
        return this.user_address;
    }

    public void setUser_address(String user_address2) {
        this.user_address = user_address2;
    }

    public String getLocality() {
        return this.locality;
    }

    public void setLocality(String locality2) {
        this.locality = locality2;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state2) {
        this.state = state2;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city2) {
        this.city = city2;
    }

    public String getPincode() {
        return this.pincode;
    }

    public void setPincode(String pincode2) {
        this.pincode = pincode2;
    }
}
