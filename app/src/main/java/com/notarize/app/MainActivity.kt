package com.notarize.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
            pinata.pinList().enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("TEST", "Something failed")
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.d("TEST", "Something worked")
                }

            })



        }
    }
}
