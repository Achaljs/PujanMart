package com.puja.mart.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private static String BASE_URL = "https://reqres.in/";
    private static String URL_FOR_SMS = "https://sms.webtextsolution.com/sms-panel/api/http/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

    public static Retrofit getRetrofitInstanceForSMS() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(URL_FOR_SMS).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
