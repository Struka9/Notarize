package com.notarize.app.db.converters

import android.net.Uri
import androidx.room.TypeConverter
import com.notarize.app.db.entities.WorkStatus
import timber.log.Timber
import java.util.*


class RoomConverters {
    @TypeConverter
    fun fromUri(uri: Uri) = uri.path.toString()

    @TypeConverter
    fun toUri(uri: String) = try {
        Uri.parse(uri)
    } catch (e: Exception) {
        Timber.e(e)
        null
    }

    @TypeConverter
    fun fromWorkStatus(workStatus: WorkStatus) = workStatus.ordinal

    @TypeConverter
    fun toWorkStatus(workStatus: Int) = try {
        WorkStatus.values()[workStatus]
    } catch (e: IndexOutOfBoundsException) {
        WorkStatus.FAILED
    }

    @TypeConverter
    fun fromCalendar(calendar: Calendar) = calendar.time.time

    @TypeConverter
    fun toCalendar(time: Long) = Calendar.getInstance().apply { timeInMillis = time }
}