package com.notarize.app.db.entities

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "work_submission")
data class WorkSubmission(
    @PrimaryKey
    val hash: String,
    val status: WorkStatus,
    val submittedOn: Calendar,
    val uri: Uri? = null
)

enum class WorkStatus {
    FAILED,
    PENDING,
    SUCCESS
}

