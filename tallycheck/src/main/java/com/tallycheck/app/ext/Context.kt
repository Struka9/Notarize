package com.tallycheck.app.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import com.tallycheck.app.R

const val PICK_IMAGE = 20

fun Activity.openPickImageDialog() {
    val getIntent = Intent(Intent.ACTION_GET_CONTENT)
    getIntent.type = "image/*"

    val pickIntent =
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    pickIntent.type = "image/*"

    val chooserIntent = Intent.createChooser(getIntent, "Select Image")
    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

    startActivityForResult(chooserIntent, PICK_IMAGE)
}

fun Context.createLoadingDialog(): AlertDialog =
    AlertDialog.Builder(this).run {
        setView(R.layout.progress_dialog)
        setCancelable(false)
    }.create().apply {
        setCanceledOnTouchOutside(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }