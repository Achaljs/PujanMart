package com.puja.mart.Modal;

import java.util.ArrayList;
import java.util.List;

public class Product {
    public List<ProductModal> productModalList;

    public List<ProductModal> getProductModalList() {
        return this.productModalList;
    }

    public void setProductModalList(ArrayList<ProductModal> productModalList2) {
        this.productModalList = productModalList2;
    }
}
