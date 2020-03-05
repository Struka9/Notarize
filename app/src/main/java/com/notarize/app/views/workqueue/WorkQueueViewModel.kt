package com.notarize.app.views.workqueue

import android.content.Context
import androidx.lifecycle.ViewModel
import com.notarize.app.db.IWorkSubmissionRepo

class WorkQueueViewModel(
    private val context: Context,
    private val workSubmissionRepo: IWorkSubmissionRepo
) : ViewModel() {

    val queueLiveData = workSubmissionRepo.getWorkSubmissions(null)
}