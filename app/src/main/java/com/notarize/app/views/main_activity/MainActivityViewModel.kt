package com.notarize.app.views.main_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.notarize.app.di.repos.ICredentialsRepo
import kotlinx.coroutines.Job
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.utils.Convert
import timber.log.Timber
import java.math.BigDecimal

class MainActivityViewModel(
    private val credentialsRepo: ICredentialsRepo,
    private val web3: Web3j
) :
    ViewModel() {

    private val job = Job()

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    val walletMoney: LiveData<BigDecimal> =
        Transformations.switchMap(credentialsRepo.credentials) { credentials ->
            return@switchMap liveData<BigDecimal>() {

                try {
                    val response =
                        web3.ethGetBalance(credentials.address, DefaultBlockParameterName.LATEST)
                            .send()

                    if (response.error == null) {
                        val wei = response.balance
                        emit(
                            Convert.fromWei(
                                wei.toString(),
                                Convert.Unit.ETHER
                            )
                        )
                    }
                } catch (e: Throwable) {
                    Timber.e(e)
                }
            }
        }

    val walletAddress: LiveData<String> =
        Transformations.map(credentialsRepo.credentials) {
            return@map it.address
        }
}