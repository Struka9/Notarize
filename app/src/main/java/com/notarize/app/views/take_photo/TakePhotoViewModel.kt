package com.notarize.app.views.take_photo

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.notarize.app.EXTRA_FILE_URI
import com.notarize.app.IpfsManager
import com.notarize.app.TallyLock
import com.notarize.app.ext.createFile
import com.notarize.app.ext.createPhotosDirIfDoesntExist
import com.notarize.app.worker.ContractWorker
import com.notarize.app.worker.IpfsWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.web3j.crypto.Credentials
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class TakePhotoViewModel(
    private val context: Context,
    private val ipfsManager: IpfsManager,
    private val notaryCredentials: Credentials,
    private val smartConract: TallyLock
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

    @SuppressLint("deprecated")
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

        val submitWorkRequest = OneTimeWorkRequestBuilder<ContractWorker>()
            .build()

        WorkManager.getInstance(context)
            .beginWith(ipfsWorkRequest)
            .then(submitWorkRequest)
            .enqueue()

    }

    private fun launchSuspendBlock(block: suspend () -> Unit): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            try {
                block()
            } catch (e: Exception) {

            } finally {
                isDoingBackgroundWork.postValue(false)
            }
        }
    }
}