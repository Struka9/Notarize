package com.notarize.app;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface PinataService {

    @GET("data/pinList?status=pinned")
    Call<JsonObject> pinList();
}
