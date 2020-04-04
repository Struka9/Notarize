package com.notarize.app.di.repos

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.web3j.crypto.CipherException
import org.web3j.crypto.Credentials
import org.web3j.crypto.MnemonicUtils
import org.web3j.crypto.WalletUtils
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.security.SecureRandom

class CredentialsRepoImpl(
    private val context: Context,
    private val sharedPreferences: SharedPreferences,
    private val keyWalletMnemonic: String,
    private val keyWalletFile: String,
    private val defaultPassword: String
) : ICredentialsRepo {
    private val walletPath = context.filesDir.absolutePath
    private val walletDir = File(walletPath)
    private val _credentials = MutableLiveData<Credentials>()
    override var credentials: LiveData<Credentials> = _credentials

    init {
        val mnemonic = sharedPreferences.getString(keyWalletMnemonic, "")
        try {
            _credentials.value = WalletUtils.loadBip39Credentials(defaultPassword, mnemonic)
        } catch (e: Throwable) {
            Timber.e(e)
        }
    }


    @Throws(CipherException::class, IOException::class)
    override suspend fun restoreWallet(mnemonic: String) {
        loadWallet(mnemonic)
    }

    @Throws(CipherException::class, IOException::class)
    override suspend fun generateWallet() {
        val secureRandom = SecureRandom()
        val entropy = secureRandom.generateSeed(20)
        val mnemonic = MnemonicUtils.generateMnemonic(entropy)
        loadWallet(mnemonic)
    }

    private fun loadWallet(mnemonic: String) {
        val wallet =
            WalletUtils.generateBip39WalletFromMnemonic(
                defaultPassword,
                mnemonic,
                walletDir
            )

        //Save the walletName
        val editor = sharedPreferences.edit()
        editor.putString(keyWalletFile, wallet.filename).putString(keyWalletMnemonic, mnemonic)
        editor.commit()
        _credentials.value = WalletUtils.loadBip39Credentials(defaultPassword, mnemonic)
    }
}