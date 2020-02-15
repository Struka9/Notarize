package com.notarize.app

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.os.StrictMode
import android.util.Log
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.MediaType


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        uploadButton.setOnClickListener{
            Log.d("TEST", "FIRST LOG MESSAGE")

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

            // Create a request body with file and image media type
            val fileReqBody = RequestBody.create(MediaType.parse("text/plain"), "This is the file content")
            // Create MultipartBody.Part using file request-body,file name and part name
            val part = MultipartBody.Part.createFormData("file", "file3.txt", fileReqBody)

            pinata.pinFileToIPFS(part).enqueue(object : Callback<JsonObject> {
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

            })
        }
    }
}
