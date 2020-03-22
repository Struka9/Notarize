package com.notarize.app.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.notarize.app.EXTRA_FILE_URI
import com.notarize.app.EXTRA_IPFS_HASH
import com.notarize.app.EXTRA_TX_HASH
import com.notarize.app.db.entities.WorkStatus
import com.notarize.app.di.repos.IContractRepo
import com.notarize.app.di.repos.IWorkSubmissionRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.web3j.protocol.core.methods.response.TransactionReceipt
import timber.log.Timber

class ContractWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters),
    KoinComponent {

    private val workSubmissionRepo: IWorkSubmissionRepo by inject()
    private val contractRepo: IContractRepo by inject()

    override val coroutineContext: CoroutineDispatcher
        get() = Dispatchers.IO

    override suspend fun doWork(): Result = coroutineScope {
        val ipfsHash =
            inputData.getString(EXTRA_IPFS_HASH) ?: return@coroutineScope Result.failure()

        val fileHash =
            inputData.getString(EXTRA_FILE_URI) ?: return@coroutineScope Result.failure()

        return@coroutineScope try {
            //If Uploaded correctly send to the SmartContract for logging
            val tallyLockContract = contractRepo.tallyLockContract

            if (tallyLockContract != null) {
                val receipt: TransactionReceipt = tallyLockContract.signDocument(
                    fileHash,
                    ipfsHash
                ).send()
                Result.success(workDataOf(EXTRA_TX_HASH to receipt.transactionHash))
            } else {
                Timber.e("Repo didn't have a contract when submitting tx")
                workSubmissionRepo
                    .updateWorkStatus(
                        fileHash,
                        WorkStatus.FAILED
                    )
                Result.failure()
            }
        } catch (e: Exception) {
            Timber.e(e)
            workSubmissionRepo
                .updateWorkStatus(
                    fileHash,
                    WorkStatus.FAILED
                )
            Result.failure()
        }
    }
}