package com.notarize.app

import android.util.Log
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class IpfsManager {

    var header1 : String
    var header2 : String

    constructor(h1 : String, h2 : String) {
        header1 = h1
        header2 = h2
    }

    fun uploadFile(fileName : String, requestBody : RequestBody, callback : Callback<IpfsObject>) {
        var retrofit = Retrofit.Builder().baseUrl("https://api.pinata.cloud/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var pinata = retrofit.create(PinataService::class.java)

        // Create MultipartBody.Part using file request-body,file name and part name
        val part = MultipartBody.Part.createFormData("file", fileName, requestBody)



        pinata.pinFileToIPFS(
            header1,
            header2,
            part).enqueue(callback)
    }
}