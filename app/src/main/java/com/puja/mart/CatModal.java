package com.puja.mart;

public class CatModal {
    public int ctgy_id;
    public String ctgy_img;
    public String ctgy_name;
    public String ctgy_slug;
    public boolean status;

    public int getCtgy_id() {
        return this.ctgy_id;
    }

    public void setCtgy_id(int ctgy_id2) {
        this.ctgy_id = ctgy_id2;
    }

    public String getCtgy_name() {
        return this.ctgy_name;
    }

    public void setCtgy_name(String ctgy_name2) {
        this.ctgy_name = ctgy_name2;
    }

    public String getCtgy_slug() {
        return this.ctgy_slug;
    }

    public void setCtgy_slug(String ctgy_slug2) {
        this.ctgy_slug = ctgy_slug2;
    }

    public boolean isStatus() {
        return this.status;
    }

    public void setStatus(boolean status2) {
        this.status = status2;
    }

    public String getCtgy_img() {
        return this.ctgy_img;
    }

    public void setCtgy_img(String ctgy_img2) {
        this.ctgy_img = ctgy_img2;
    }
}
