package com.notarize.app.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.notarize.app.EXTRA_FILE_URI
import com.notarize.app.EXTRA_IPFS_HASH
import com.notarize.app.EXTRA_TX_HASH
import com.notarize.app.TallyLock
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.web3j.crypto.Credentials
import org.web3j.protocol.core.methods.response.TransactionReceipt
import timber.log.Timber

class ContractWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters),
    KoinComponent {

    private val notaryCredentials: Credentials by inject()
    private val smartConract: TallyLock by inject()

    override val coroutineContext: CoroutineDispatcher
        get() = Dispatchers.IO

    override suspend fun doWork(): Result = coroutineScope {
        val ipfsHash =
            inputData.getString(EXTRA_IPFS_HASH) ?: return@coroutineScope Result.failure()

        val fileHash =
            inputData.getString(EXTRA_FILE_URI) ?: return@coroutineScope Result.failure()

        return@coroutineScope try {
            //If Uploaded correctly send to the SmartContract for logging
            val receipt: TransactionReceipt = smartConract
                .signDocument(
                    fileHash,
                    ipfsHash
                ).send()

            Result.success(workDataOf(EXTRA_TX_HASH to receipt.transactionHash))
        } catch (e: Exception) {
            Timber.e(e)
            Result.failure()
        }
    }
}