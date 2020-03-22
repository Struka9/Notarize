package com.notarize.app.di.repos

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.web3j.crypto.Credentials
import org.web3j.crypto.WalletUtils
import timber.log.Timber
import java.io.File

class CredentialsRepoImpl(
    private val context: Context,
    private val sharedPreferences: SharedPreferences,
    private val keyWalletFile: String,
    private val defaultPassword: String
) : ICredentialsRepo {
    private val _credentials = MutableLiveData<Credentials>()
    override var credentials: LiveData<Credentials> = _credentials

    init {
        val currentWalletFileName = sharedPreferences.getString(keyWalletFile, "")
        val walletPath = context.filesDir.absolutePath
        val walletDir = File(walletPath)
        val walletFile = File(walletDir, currentWalletFileName)
        try {
            _credentials.value = WalletUtils.loadCredentials(defaultPassword, walletFile)
        } catch (e: Throwable) {
            Timber.e(e)
        }
    }

    override suspend fun generateWallet() {
        val walletPath = context.filesDir.absolutePath
        val walletDir = File(walletPath)

        //There is no wallet, create the wallet
        try {
            val walletName =
                WalletUtils.generateLightNewWalletFile(defaultPassword, walletDir)
            //Save the walletName
            val editor = sharedPreferences.edit()
            editor.putString(keyWalletFile, walletName)
            editor.commit()
            val walletFile = File(walletDir, walletName)
            _credentials.value = WalletUtils.loadCredentials(defaultPassword, walletFile)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}