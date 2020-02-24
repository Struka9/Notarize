package com.notarize.app.views.take_photo

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notarize.app.IpfsManager
import com.notarize.app.TallyLock
import com.notarize.app.ext.createFile
import com.notarize.app.ext.createPhotosDirIfDoesntExist
import com.notarize.app.ext.toHexString
import com.notarize.app.ext.toSha256
import com.notarize.app.toByteArray
import io.jsonwebtoken.io.Encoders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import org.web3j.crypto.Credentials
import org.web3j.crypto.Hash
import org.web3j.crypto.Sign
import org.web3j.protocol.core.methods.response.TransactionReceipt
import java.io.File
import java.util.*
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

    fun submitDocument() {

        isDoingBackgroundWork.value = true

        launchSuspendBlock {
            val fileHash: String =
                MediaStore.Images.Media.getBitmap(context.contentResolver, photoFileUri.value)
                    .toByteArray()
                    .toSha256().toHexString()

            val jwtHeader = JSONObject()
            jwtHeader.put("typ", "JWT")
                .put("alg", "ES256")

            val jwtPayload = JSONObject()
            jwtPayload.put("iss", notaryCredentials.address)
                .put("fileHash", fileHash)
                .put("isa", Date().time)

            var encodedMessage =
                Encoders.BASE64URL.encode(jwtHeader.toString().toByteArray()) + "." + Encoders.BASE64URL.encode(
                    jwtPayload.toString().toByteArray()
                )
            val signatureData = Sign.signMessage(
                Hash.sha256(encodedMessage.toByteArray()),
                notaryCredentials.ecKeyPair
            )
            encodedMessage += "." + Encoders.BASE64URL.encode(signatureData.toString().toByteArray())

            val jwtToken = encodedMessage

            // Create a request body with file and image media type
            val fileReqBody = RequestBody.create(MediaType.parse("application/jwt"), jwtToken)
            val fileName = "filehash.jwt"

            val response = ipfsManager.uploadFile(fileName, fileReqBody)
            if (response.code() == 200) {
                //var smartContract = TallyLock.load(getString(R.string.tallyLockSmartContractAddress), web3, notaryCredentials, DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT)

                val body = response.body()!!
                //If Uploaded correctly send to the SmartContract for logging
                val receipt: TransactionReceipt = smartConract
                    .signDocument(
                        fileHash,
                        body.IpfsHash
                    ).send()

                Log.d("TEST", receipt.transactionHash)
            } else {
                Log.d("TEST", "Something Kind Of worked")
                Log.d("TEST", "CODE " + response.code())
            }
        }
    }

    private fun launchSuspendBlock(block: suspend () -> Unit): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            try {
                block()
            } catch (e: Exception) {
                isDoingBackgroundWork.postValue(false)
            } finally {
                isDoingBackgroundWork.postValue(false)
            }
        }
    }
}