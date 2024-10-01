package com.puja.mart.Modal;

import com.google.gson.annotations.SerializedName;

public class imageModel {

    @SerializedName("product_img")
    String image;

    public imageModel(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
