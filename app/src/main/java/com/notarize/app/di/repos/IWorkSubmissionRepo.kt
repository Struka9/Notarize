package com.notarize.app.di.repos

import androidx.lifecycle.LiveData
import com.notarize.app.db.entities.WorkStatus
import com.notarize.app.db.entities.WorkSubmission

interface IWorkSubmissionRepo {
    fun getWorkSubmissions(status: WorkStatus? = null): LiveData<List<WorkSubmission>>
    suspend fun insert(workSubmission: WorkSubmission)
    suspend fun delete(status: WorkStatus? = null)
    suspend fun updateWork(workSubmission: WorkSubmission)
    suspend fun updateWorkStatus(hash: String, workStatus: WorkStatus)
}