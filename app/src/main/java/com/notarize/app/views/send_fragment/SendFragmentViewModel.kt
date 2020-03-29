package com.notarize.app.views.send_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notarize.app.Resource
import com.notarize.app.Status
import com.notarize.app.di.repos.ICredentialsRepo
import com.notarize.app.ext.isNullOrZero
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.web3j.protocol.Web3j
import org.web3j.tx.Transfer
import org.web3j.utils.Convert
import java.math.BigDecimal

class SendFragmentViewModel(
    private val web3j: Web3j,
    private val credentialsRepo: ICredentialsRepo
) : ViewModel() {

    private val job = Job()

    val destinationAddress = MutableLiveData<String>()
    val amount = MutableLiveData<BigDecimal>()
    val message = MutableLiveData<String>()

    private val _transactionStatus = MutableLiveData<Resource<String>>()
    val transactionStatus = _transactionStatus

    fun sendToAddress() {
        _transactionStatus.value = Resource(Status.LOADING)

        val amountValue = amount.value
        val toAddressValue = destinationAddress.value
        val fromAddressValue = credentialsRepo.credentials.value

        if (toAddressValue.isNullOrBlank() || amountValue.isNullOrZero() || fromAddressValue == null) {
            _transactionStatus.value = Resource(Status.ERROR)
        } else {
            viewModelScope.launch(job + Dispatchers.IO) {
                val receipt = Transfer.sendFunds(
                    web3j,
                    fromAddressValue,
                    toAddressValue,
                    amountValue,
                    Convert.Unit.ETHER
                ).send()

                transactionStatus.postValue(
                    Resource(
                        Status.SUCCESS,
                        data = receipt.transactionHash
                    )
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}

