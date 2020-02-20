package com.notarize.app.views.take_photo

import android.content.Context
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.notarize.app.ext.createFile
import com.notarize.app.ext.createPhotosDirIfDoesntExist
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class TakePhotoViewModel(private val context: Context) : ViewModel() {

    private val executor: Executor by lazy { Executors.newSingleThreadExecutor() }

    val showProgressBar = MutableLiveData<Boolean>().apply {
        value = false
    }

    fun savePictureToFile(imageCapture: ImageCapture?) {
        showProgressBar.value = true
        context.createPhotosDirIfDoesntExist()
        val file = context.createFile()

        imageCapture?.takePicture(
            file,
            executor,
            object : ImageCapture.OnImageSavedListener {
                override fun onImageSaved(file: File) {
                    val uri =
                        FileProvider.getUriForFile(
                            context,
                            context.packageName,
                            file
                        )

                    Log.d("TakePhoto", "URI is $uri")
                    showProgressBar.postValue(false)
                }

                override fun onError(
                    imageCaptureError: ImageCapture.ImageCaptureError,
                    message: String,
                    cause: Throwable?
                ) {
                    Log.e("TakePhoto", message, cause)
                    showProgressBar.postValue(false)
                }

            })
    }
}