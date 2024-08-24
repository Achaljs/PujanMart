package com.puja.mart.Modal;

public class CartModal {
    public String create_date;
    public int id;
    public int price;
    public String product_id;
    public String product_name;
    public String product_thumb;
    public int quantity;
    public String update_date;
    public String user_id;

    public int getId() {
        return this.id;
    }

    public void setId(int id2) {
        this.id = id2;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id2) {
        this.user_id = user_id2;
    }

    public String getProduct_id() {
        return this.product_id;
    }

    public void setProduct_id(String product_id2) {
        this.product_id = product_id2;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price2) {
        this.price = price2;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity2) {
        this.quantity = quantity2;
    }

    public String getCreate_date() {
        return this.create_date;
    }

    public void setCreate_date(String create_date2) {
        this.create_date = create_date2;
    }

    public String getUpdate_date() {
        return this.update_date;
    }

    public void setUpdate_date(String update_date2) {
        this.update_date = update_date2;
    }

    public String getProduct_name() {
        return this.product_name;
    }

    public void setProduct_name(String product_name2) {
        this.product_name = product_name2;
    }

    public String getProduct_thumb() {
        return this.product_thumb;
    }

    public void setProduct_thumb(String product_thumb2) {
        this.product_thumb = product_thumb2;
    }
}
