package com.notarize.app.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.notarize.app.db.converters.RoomConverters
import com.notarize.app.db.dao.WorkSubmissionDAO
import com.notarize.app.db.entities.WorkSubmission

@Database(entities = [WorkSubmission::class], version = 1)
@TypeConverters(RoomConverters::class)
abstract class TallyLockDatabase : RoomDatabase() {
    abstract fun workSubmissionsDao(): WorkSubmissionDAO

    companion object {
        @Volatile
        private var database: TallyLockDatabase? = null

        fun getDatabase(context: Context): TallyLockDatabase {
            val tmpInstance = database
            if (tmpInstance != null) return tmpInstance

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TallyLockDatabase::class.java,
                    "tallylock"
                ).build()

                database = instance
                return instance
            }
        }
    }
}