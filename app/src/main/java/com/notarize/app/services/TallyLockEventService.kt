package com.notarize.app.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.notarize.app.TallyLock
import com.notarize.app.db.IWorkSubmissionRepo
import com.notarize.app.db.entities.WorkStatus
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class TallyLockEventService : Service() {

    private lateinit var disposable: Disposable
    private val job = Job()
    private val coroutineContext: CoroutineContext = Dispatchers.IO + job

    // TODO: Listen for changes in wallet address
    private val credentials: Credentials by inject()
    private val tallyLockContract: TallyLock by inject()
    private val web3: Web3j by inject()
    private val workSubmissionRepo: IWorkSubmissionRepo by inject()

    override fun onCreate() {
        super.onCreate()
        val address = tallyLockContract.contractAddress
        disposable = tallyLockContract
            .logDocumentSignedEventFlowable(
                DefaultBlockParameterName.LATEST,
                DefaultBlockParameterName.PENDING
            )
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .doOnError {
                Timber.e(it)
            }
            .subscribe({
                GlobalScope.launch(coroutineContext) {
                    Timber.d("Updating document ${it._documentHash}")
                    workSubmissionRepo.updateWorkStatus(it._documentHash, WorkStatus.SUCCESS)
                }
            }) {
                // On error
                Timber.e(it)
            }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        job.complete()
        disposable.dispose()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}