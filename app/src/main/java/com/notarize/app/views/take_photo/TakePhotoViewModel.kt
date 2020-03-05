package com.notarize.app.views.take_photo

import android.content.Context
import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.notarize.app.EXTRA_FILE_URI
import com.notarize.app.TAG_SUBMIT_DOC
import com.notarize.app.ext.createFile
import com.notarize.app.ext.createPhotosDirIfDoesntExist
import com.notarize.app.worker.ContractWorker
import com.notarize.app.worker.IpfsWorker
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class TakePhotoViewModel(
    private val context: Context
) : ViewModel() {

    private val executor: Executor by lazy { Executors.newSingleThreadExecutor() }
    val photoFileUri = MutableLiveData<Uri>()


    val isDoingBackgroundWork = MutableLiveData<Boolean>().apply {
        value = false
    }

    fun savePictureToFile(imageCapture: ImageCapture?) {
        isDoingBackgroundWork.value = true
        context.createPhotosDirIfDoesntExist()
        val file = context.createFile()

        imageCapture?.takePicture(
            file,
            executor,
            object : ImageCapture.OnImageSavedListener {
                override fun onImageSaved(file: File) {
                    photoFileUri.postValue(
                        FileProvider.getUriForFile(
                            context,
                            context.packageName,
                            file
                        )
                    )
                    isDoingBackgroundWork.postValue(false)
                }

                override fun onError(
                    imageCaptureError: ImageCapture.ImageCaptureError,
                    message: String,
                    cause: Throwable?
                ) {
                    isDoingBackgroundWork.postValue(false)
                }
            })
    }

    fun submitDocument() {
        if (photoFileUri.value == null) return

        val constraints = Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val ipfsWorkRequest = OneTimeWorkRequestBuilder<IpfsWorker>()
            .setConstraints(constraints)
            .setInputData(workDataOf(EXTRA_FILE_URI to photoFileUri.value?.toString()))
            .build()

        val submitWorkRequest =
            OneTimeWorkRequestBuilder<ContractWorker>()
                .addTag(TAG_SUBMIT_DOC)
                .build()

        WorkManager.getInstance(context)
            .beginWith(ipfsWorkRequest)
            .then(submitWorkRequest)
            .enqueue()

    }
}