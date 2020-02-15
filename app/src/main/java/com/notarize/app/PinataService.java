package com.notarize.app;

import android.content.res.Resources;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PinataService {

    @GET("data/pinList?status=pinned")
    Call<JsonObject> pinList();

    @Multipart
    @POST("pinning/pinFileToIPFS")
    Call<JsonObject> pinFileToIPFS(
            @Header("pinata_api_key") String pinataAPIKey,
            @Header("pinata_secret_api_key") String pinataAPISecret,
            @Part MultipartBody.Part file);
}
