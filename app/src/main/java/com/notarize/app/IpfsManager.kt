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

    fun uploadFile(fileName : String, requestBody : RequestBody) {
        var retrofit = Retrofit.Builder().baseUrl("https://api.pinata.cloud/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var pinata = retrofit.create(PinataService::class.java)


        /*pinata.pinList().enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("TEST", t.message)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.code() == 200) {
                    Log.d("TEST", "Something worked")
                    val body = response.body()!!
                    Log.d("TEST", body.toString())
                } else {
                    Log.d("TEST", "Something Kind Of worked")
                    Log.d("TEST", "CODE " + response.code())
                }
            }

        })*/

        // Create MultipartBody.Part using file request-body,file name and part name
        val part = MultipartBody.Part.createFormData("file", fileName, requestBody)



        pinata.pinFileToIPFS(
            header1,
            header2,
            part).enqueue(object : Callback<IpfsObject> {
            override fun onFailure(call: Call<IpfsObject>, t: Throwable) {
                Log.d("TEST", t.message)
            }

            override fun onResponse(call: Call<IpfsObject>, response: Response<IpfsObject>) {

                if (response.code() == 200) {
                    Log.d("TEST", "Something worked")
                    val body = response.body()!!
                    Log.d("TEST", body.IpfsHash)
                } else {
                    Log.d("TEST", "Something Kind Of worked")
                    Log.d("TEST", "CODE " + response.code())
                }
            }

        })
    }
}