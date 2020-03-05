package com.notarize.app.db

import androidx.lifecycle.LiveData
import com.notarize.app.db.dao.WorkSubmissionDAO
import com.notarize.app.db.entities.WorkStatus
import com.notarize.app.db.entities.WorkSubmission

class WorkSubmissionRepo(private val workSubmissionDAO: WorkSubmissionDAO) : IWorkSubmissionRepo {
    override suspend fun insert(workSubmission: WorkSubmission) {
        workSubmissionDAO.insertWorkSubmission(workSubmission)
    }

    override fun getWorkSubmissions(status: WorkStatus?): LiveData<List<WorkSubmission>> {
        return if (status == null) {
            workSubmissionDAO.getAllWork()
        } else {
            workSubmissionDAO.getWorkByStatus(status)
        }
    }

    override suspend fun delete(status: WorkStatus?) {
        if (status == null) {
            workSubmissionDAO.deleteAll()
        } else {
            workSubmissionDAO.deleteAllWithStatus(status)
        }
    }

    override suspend fun updateWork(workSubmission: WorkSubmission) {
        workSubmissionDAO.updateWorkSubmission(workSubmission)
    }

    override suspend fun updateWorkStatus(hash: String, workStatus: WorkStatus) {
        workSubmissionDAO.updateWorkStatus(hash, workStatus)
    }
}