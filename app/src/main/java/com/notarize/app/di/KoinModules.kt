package com.notarize.app.di

import android.content.ClipboardManager
import android.content.Context
import com.notarize.app.IpfsManager
import com.notarize.app.R
import com.notarize.app.SettingsFragmentViewModel
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
import org.web3j.protocol.Web3j
import org.web3j.protocol.websocket.WebSocketService
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

    single(qualifier = StringQualifier("mnemonic-pref-key")) {
        androidContext().getString(R.string.ok)
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
        val context = androidContext()
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    single {
        IpfsManager(
            get(qualifier = StringQualifier("header1")),
            get(qualifier = StringQualifier("header2"))
        )
    }

    single {
        val network = get<String>(qualifier = StringQualifier("network-gateway"))
        val ws = WebSocketService(
            network,
            true
        )
        ws.connect()

        return@single Web3j.build(ws)
    }

    single<ContractGasProvider> {
        DefaultGasProvider()
    }

    single {
        val context = get<Context>()
        context.getSharedPreferences(
            context.getString(R.string.k_WalletFileName),
            Context.MODE_PRIVATE
        )
    }

    single<ICredentialsRepo> {
        CredentialsRepoImpl(
            get(),
            get(),
            get(StringQualifier("mnemonic-pref-key")),
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

    viewModel {
        SettingsFragmentViewModel(
            get(),
            get(),
            get(StringQualifier("mnemonic-pref-key")),
            get(),
            get()
        )
    }
}