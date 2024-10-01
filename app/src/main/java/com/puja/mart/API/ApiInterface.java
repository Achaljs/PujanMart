package com.puja.mart.API;

import com.puja.mart.CatModal;
import com.puja.mart.Modal.CartModal;
import com.puja.mart.Modal.Order;
import com.puja.mart.Modal.ProductModal;
import com.puja.mart.Modal.SMSModal;
import com.puja.mart.Modal.imageModel;
import com.puja.mart.ServiceModle;
import com.puja.mart.SubCatModal;
import com.puja.mart.ui.OrderPost;


import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiInterface {
    @POST("https://api.pujanmart.com/checkout_cod")
    @Headers({"Content-Type: application/json"})
    Call<Integer> createPost(@Body OrderPost orderPost);


    @POST("https://api.pujanmart.com/updateorderonline")
    Call<JSONObject> updateOrderStatus(
            @HeaderMap Map<String, String> headers,
            @Body String requestBody
    );

    @GET
    Call<List<CartModal>> getCartList(@Url String str);

    @GET
    Call<List<CatModal>> getCatList(@Url String str);

    @GET
    Call<List<ServiceModle>> getServList(@Url String str);

    @GET
    Call<List<ProductModal>> getProuctList(@Url String str);

    @GET
    Call<List<imageModel>> getImageList(@Url String str);

    @GET
    Call<List<SubCatModal>> getSubCatList(@Url String str);

    @GET
    Call<List<Order>> getOrderList(@Url String str);

    @GET
    Call<String> send(@Url String str);

    @GET
    Call<SMSModal> sendSMS(@Url String str);
}
