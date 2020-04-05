package com.notarize.app.views.signed_doc

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.notarize.app.db.entities.SignedDoc
import com.notarize.app.di.repos.IContractRepo

class SignedDocViewModel(private val contractRepo: IContractRepo) : ViewModel() {

    val signedDocs =
        Transformations.switchMap(contractRepo.tallyLockContractLiveData) { tallyLock ->
            return@switchMap liveData<SignedDoc> {

            }
        }
}