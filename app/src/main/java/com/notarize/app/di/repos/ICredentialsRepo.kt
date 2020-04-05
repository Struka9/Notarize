package com.notarize.app.di.repos

import androidx.lifecycle.LiveData
import org.web3j.crypto.CipherException
import org.web3j.crypto.Credentials
import java.io.IOException

interface ICredentialsRepo {
    val credentials: LiveData<Credentials>

    suspend fun restoreWallet(mnemonic: String)

    @Throws(CipherException::class, IOException::class)
    suspend fun generateWallet()
}