package com.puja.mart;

public class SubCatModal {
    public boolean status;
    public int subctgy_id;
    public String subctgy_img;
    public String subctgy_name;
    public String subctgy_slug;

    public int getSubctgy_id() {
        return this.subctgy_id;
    }

    public void setSubctgy_id(int subctgy_id2) {
        this.subctgy_id = subctgy_id2;
    }

    public String getSubctgy_name() {
        return this.subctgy_name;
    }

    public void setSubctgy_name(String subctgy_name2) {
        this.subctgy_name = subctgy_name2;
    }

    public String getSubctgy_slug() {
        return this.subctgy_slug;
    }

    public void setSubctgy_slug(String subctgy_slug2) {
        this.subctgy_slug = subctgy_slug2;
    }

    public String getSubctgy_img() {
        return this.subctgy_img;
    }

    public void setSubctgy_img(String subctgy_img2) {
        this.subctgy_img = subctgy_img2;
    }

    public boolean isStatus() {
        return this.status;
    }

    public void setStatus(boolean status2) {
        this.status = status2;
    }
}
