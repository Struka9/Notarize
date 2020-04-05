package com.notarize.app

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.notarize.app.di.repos.ICredentialsRepo
import com.notarize.app.di.repos.IWorkSubmissionRepo
import kotlinx.coroutines.launch
import org.web3j.protocol.Web3j

class SettingsFragmentViewModel(
    private val web3: Web3j,
    private val sharedPreferences: SharedPreferences,
    private val kMnemonicPhrase: String,
    private val credentialsRepo: ICredentialsRepo,
    private val workSubmissionRepo: IWorkSubmissionRepo
) : ViewModel() {

    fun exportWallet() = liveData {
        emit(Resource(Status.LOADING))
        val mnemonic = sharedPreferences.getString(kMnemonicPhrase, "")
        if (mnemonic.isNullOrBlank()) emit(Resource(Status.ERROR))
        else emit(Resource(Status.SUCCESS, data = mnemonic))
    }

    fun importWallet(mnemonic: String) = liveData<Resource<String>> {
        emit(Resource(Status.LOADING))
        try {
            credentialsRepo.restoreWallet(mnemonic)
            emit(Resource(Status.SUCCESS))
        } catch (e: Exception) {
            emit(Resource(Status.ERROR))
        }
    }

    fun generateNewWallet() = liveData<Resource<Unit>> {
        emit(Resource(Status.LOADING))
        viewModelScope.launch {
            try {
                credentialsRepo.generateWallet()
                workSubmissionRepo.delete()
                emit(Resource(Status.SUCCESS))
            } catch (e: Exception) {
                emit(Resource(Status.ERROR))
            }
        }
    }
}