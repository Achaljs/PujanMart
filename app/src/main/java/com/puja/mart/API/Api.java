package com.puja.mart.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private static String BASE_URL = "https://api.pujanmart.com/";
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
    public static Retrofit getService(){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl("https://api.pujanmart.com/").addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
