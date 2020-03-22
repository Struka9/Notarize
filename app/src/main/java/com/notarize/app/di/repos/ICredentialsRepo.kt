package com.notarize.app.di.repos

import androidx.lifecycle.LiveData
import org.web3j.crypto.Credentials

interface ICredentialsRepo {
    val credentials: LiveData<Credentials>

    suspend fun generateWallet()
}