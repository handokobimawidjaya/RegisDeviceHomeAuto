package com.bandung.adddevice.Support;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST("admin/login")
    Call<ResponseBody> userLogin(
            @Field("email") String email,
            @Field("password") String password
    );
}
