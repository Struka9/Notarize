package com.notarize.app.ext

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import com.notarize.app.R

fun Context.createLoadingDialog(): AlertDialog =
    AlertDialog.Builder(this).run {
        setView(R.layout.progress_dialog)
        setCancelable(false)
    }.create().apply {
        setCanceledOnTouchOutside(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }