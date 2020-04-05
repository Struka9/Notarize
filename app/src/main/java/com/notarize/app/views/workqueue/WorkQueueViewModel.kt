package com.notarize.app.views.workqueue

import androidx.lifecycle.ViewModel
import com.notarize.app.di.repos.IWorkSubmissionRepo

class WorkQueueViewModel(
    private val workSubmissionRepo: IWorkSubmissionRepo
) : ViewModel() {
    val queueLiveData = workSubmissionRepo.getWorkSubmissions(null)
}