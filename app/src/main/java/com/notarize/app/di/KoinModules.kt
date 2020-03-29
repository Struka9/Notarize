package com.notarize.app.di

import android.content.Context
import com.notarize.app.EthereumManager
import com.notarize.app.IpfsManager
import com.notarize.app.R
import com.notarize.app.db.TallyLockDatabase
import com.notarize.app.di.repos.*
import com.notarize.app.views.main_activity.MainActivityViewModel
import com.notarize.app.views.send_fragment.SendFragmentViewModel
import com.notarize.app.views.take_photo.TakePhotoViewModel
import com.notarize.app.views.workqueue.WorkQueueViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import org.web3j.tx.gas.ContractGasProvider
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

    single<ContractGasProvider> {
        DefaultGasProvider()
    }

    single {
        val context = get<Context>()
        context.getSharedPreferences(
            context.getString(com.tallycheck.tallycheckcontract.R.string.k_WalletSharedPreferences),
            Context.MODE_PRIVATE
        )
    }

    single<ICredentialsRepo> {
        CredentialsRepoImpl(
            get(),
            get(),
            get(StringQualifier("wallet-pref-key")),
            get(StringQualifier("wallet-password"))
        )
    }

    single<IContractRepo> {
        ContractRepoImpl(
            get(),
            get(qualifier = StringQualifier("smart-contract-address")),
            get(),
            get()
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
            get()
        )
    }

    viewModel {
        MainActivityViewModel(
            get(),
            get()
        )
    }

    viewModel {
        SendFragmentViewModel(get(), get())
    }
}