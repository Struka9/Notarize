package com.notarize.app;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;

public interface PinataService {

    @GET("data/pinList?status=pinned")
    Call<String> pinList();
}
