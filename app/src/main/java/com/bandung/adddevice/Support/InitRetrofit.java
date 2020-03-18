package com.bandung.adddevice.Support;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InitRetrofit {
    public static final String BASE_URL = "http://192.168.14.15:3000/";
    private static InitRetrofit mInstance;
    private Retrofit retrofit;

    private InitRetrofit(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory( GsonConverterFactory.create())
                .build();
    }

    public static synchronized InitRetrofit getInstance(){
        if (mInstance == null ){
            mInstance = new InitRetrofit();
        }
        return mInstance;
    }

    public ApiService getApi(){
        return retrofit.create(ApiService.class);
    }
}
