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

            /*
            // Create a request body with file and image media type
            val fileReqBody = RequestBody.create(MediaType.parse("text/plain"), "This is the file content")
            val fileName = "file3.txt"

            val ipfsManager = IpfsManager(
                getString(R.string.pinata_header_1),
                getString(R.string.pinata_header_2))

            ipfsManager.uploadFile(fileName, fileReqBody)
             */

            var ethereumManager = EthereumManager()
            ethereumManager.connectToNetwork(getString(R.string.testnet_infura_endpoint))
        }
    }
}
