package com.notarize.app.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.notarize.app.db.entities.WorkStatus
import com.notarize.app.db.entities.WorkSubmission

@Dao
interface WorkSubmissionDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkSubmission(workSubmission: WorkSubmission)

    @Update
    suspend fun updateWorkSubmission(workSubmission: WorkSubmission)

    @Query("UPDATE work_submission SET status = :newStatus WHERE hash = :hash")
    suspend fun updateWorkStatus(hash: String, newStatus: WorkStatus)

    @Delete
    suspend fun deleteSubmission(workSubmission: WorkSubmission)

    @Query("DELETE FROM work_submission")
    suspend fun deleteAll()

    @Query("DELETE FROM work_submission WHERE status = :status")
    suspend fun deleteAllWithStatus(status: WorkStatus)

    @Query("SELECT * FROM work_submission WHERE status = :status")
    fun getWorkByStatus(status: WorkStatus): LiveData<List<WorkSubmission>>

    @Query("SELECT * FROM work_submission")
    fun getAllWork(): LiveData<List<WorkSubmission>>
}