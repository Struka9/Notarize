package com.notarize.app.di.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.notarize.app.TallyLock
import org.web3j.protocol.Web3j
import org.web3j.tx.gas.ContractGasProvider
import timber.log.Timber

class ContractRepoImpl(
    private val credentialsRepo: ICredentialsRepo,
    private val contractAddress: String,
    private val web3: Web3j,
    private val contractGasProvider: ContractGasProvider
) : IContractRepo {

    private var _contract: TallyLock? = null

    override val tallyLockContract: TallyLock?
        get() = _contract

    override val tallyLockContractLiveData: LiveData<TallyLock>
        get() = Transformations.switchMap(credentialsRepo.credentials) { credentials ->
            Timber.d("Getting new credentials on contract repo")
            val contract = TallyLock.load(
                contractAddress,
                web3,
                credentials,
                contractGasProvider
            )
            _contract = contract
            return@switchMap MutableLiveData(
                contract
            )
        }
}