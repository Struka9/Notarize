package com.notarize.app.di.repos

import androidx.lifecycle.LiveData
import com.notarize.app.TallyLock

interface IContractRepo {
    val tallyLockContract: TallyLock?
    val tallyLockContractLiveData: LiveData<TallyLock>
}