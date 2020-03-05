package com.notarize.app

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class IpfsManager(private val header1: String,
                  private val header2: String) {
    suspend fun uploadFile(fileName: String, requestBody: RequestBody): Response<IpfsObject> {
        // TODO: Remove retrofit declaration
        val retrofit = Retrofit.Builder().baseUrl("https://api.pinata.cloud/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val pinata = retrofit.create(PinataService::class.java)

        // Create MultipartBody.Part using file request-body,file name and part name
        val part = MultipartBody.Part.createFormData("file", fileName, requestBody)

        return pinata.pinFileToIPFS(
            header1,
            header2,
            part
        ).execute()
    }
}