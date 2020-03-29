package com.notarize.app.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "signed_doc")
data class SignedDoc(
    @PrimaryKey
    val hash: String
)