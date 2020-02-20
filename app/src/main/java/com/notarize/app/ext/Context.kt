package com.notarize.app.ext

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import com.notarize.app.R
import java.io.File

const val PHOTO_PREFIX = "photo_"
const val PHOTO_SUFFIX = ".jpg"

fun Context.createLoadingDialog(): AlertDialog =
    AlertDialog.Builder(this).run {
        setView(R.layout.progress_dialog)
        setCancelable(false)
    }.create().apply {
        setCanceledOnTouchOutside(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

fun Context.createPhotosDirIfDoesntExist() {
    val photosDir = File(cacheDir, "${File.separator}photos")
    if (!photosDir.exists()) {
        photosDir.mkdirs()
    }
}

fun Context.createFile() =
    File(
        "$cacheDir${File.separator}photos",
        "$PHOTO_PREFIX${System.currentTimeMillis()}$PHOTO_SUFFIX"
    )