package com.puja.mart.API;

import com.puja.mart.CatModal;
import com.puja.mart.Modal.CartModal;
import com.puja.mart.Modal.ProductModal;
import com.puja.mart.Modal.SMSModal;
import com.puja.mart.SubCatModal;
import com.puja.mart.ui.OrderPost;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiInterface {
    @POST("https://api.pujanmart.com/checkout_cod")
    @Headers({"Content-Type: application/json"})
    Call<Integer> createPost(@Body OrderPost orderPost);

    @GET
    Call<List<CartModal>> getCartList(@Url String str);

    @GET
    Call<List<CatModal>> getCatList(@Url String str);

    @GET
    Call<List<ProductModal>> getProuctList(@Url String str);

    @GET
    Call<List<SubCatModal>> getSubCatList(@Url String str);

    @GET
    Call<String> send(@Url String str);

    @GET
    Call<SMSModal> sendSMS(@Url String str);
}
