package com.notarize.app.di

import com.notarize.app.EthereumManager
import com.notarize.app.IpfsManager
import com.notarize.app.R
import com.notarize.app.TallyLock
import com.notarize.app.db.IWorkSubmissionRepo
import com.notarize.app.db.TallyLockDatabase
import com.notarize.app.db.WorkSubmissionRepo
import com.notarize.app.views.take_photo.TakePhotoViewModel
import com.notarize.app.views.workqueue.WorkQueueViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.tx.gas.DefaultGasProvider

val appModule = module {
    single(qualifier = StringQualifier("smart-contract-address")) {
        androidContext().getString(R.string.tallyLockSmartContractAddress)
    }

    single(qualifier = StringQualifier("network-gateway")) {
        androidContext().getString(R.string.testnet_infura_endpoint)
    }

    single(qualifier = StringQualifier("wallet-pref-key")) {
        androidContext().getString(R.string.k_WalletSharedPreferences)
    }

    single(qualifier = StringQualifier("wallet-password")) {
        androidContext().getString(R.string.temp_password)
    }

    single(qualifier = StringQualifier("header1")) {
        androidContext().getString(R.string.pinata_header_1)
    }

    single(qualifier = StringQualifier("header2")) {
        androidContext().getString(R.string.pinata_header_2)
    }

    single {
        IpfsManager(
            get(qualifier = StringQualifier("header1")),
            get(qualifier = StringQualifier("header2"))
        )
    }

    single {
        EthereumManager()
    }

    single {
        val ethereumManager: EthereumManager = get()
        ethereumManager.connectToNetwork(get<String>(qualifier = StringQualifier("network-gateway")))
    }

    single {
        val ethereumManager: EthereumManager = get()
        val context = androidContext()
        ethereumManager.loadCredentials(
            context,
            get(qualifier = StringQualifier("wallet-pref-key")),
            get<String>(qualifier = StringQualifier("wallet-password"))
        )
    }

    single {
        TallyLock.load(
            get<String>(qualifier = StringQualifier("smart-contract-address")),
            get<Web3j>(),
            get<Credentials>(),
            DefaultGasProvider.GAS_PRICE,
            DefaultGasProvider.GAS_LIMIT
        )
    }

    // DB Dependencies
    single {
        val context = androidContext()
        val db = TallyLockDatabase.getDatabase(context)
        db
    }

    single {
        get<TallyLockDatabase>().workSubmissionsDao()
    }

    single<IWorkSubmissionRepo> {
        WorkSubmissionRepo(get())
    }

    viewModel {
        TakePhotoViewModel(
            get()
        )
    }

    viewModel {
        WorkQueueViewModel(
            get(),
            get()
        )
    }
}