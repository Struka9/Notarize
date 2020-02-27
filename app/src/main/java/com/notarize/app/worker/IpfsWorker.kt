package com.notarize.app.worker

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.notarize.app.EXTRA_FILE_URI
import com.notarize.app.EXTRA_IPFS_HASH
import com.notarize.app.IpfsManager
import com.notarize.app.ext.toHexString
import com.notarize.app.ext.toSha256
import com.notarize.app.toByteArray
import io.jsonwebtoken.io.Encoders
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.web3j.crypto.Credentials
import org.web3j.crypto.Hash
import org.web3j.crypto.Sign
import java.util.*

class IpfsWorker(private val context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters), KoinComponent {

    private val ipfsManager: IpfsManager by inject()
    private val notaryCredentials: Credentials by inject()

    override val coroutineContext: CoroutineDispatcher
        get() = Dispatchers.IO

    override suspend fun doWork(): Result = coroutineScope {
        val fileUri = inputData.getString(EXTRA_FILE_URI)
        if (fileUri.isNullOrBlank()) return@coroutineScope Result.failure()

        val fileHash: String =
            MediaStore.Images.Media.getBitmap(context.contentResolver, Uri.parse(fileUri))
                .toByteArray()
                .toSha256()
                .toHexString()

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

        return@coroutineScope if (response.code() == 200) {
            val hash = response.body()!!.IpfsHash

            Result.success(workDataOf(EXTRA_IPFS_HASH to hash, EXTRA_FILE_URI to fileUri))
        } else {
            Result.failure()
        }
    }
}