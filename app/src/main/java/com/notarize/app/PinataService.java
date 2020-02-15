package com.notarize.app;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PinataService {

    @GET("data/pinList?status=pinned")
    Call<JsonObject> pinList();

    @Multipart
    @POST("pinning/pinFileToIPFS")
    Call<JsonObject> pinFileToIPFS(@Part MultipartBody.Part file);
}
