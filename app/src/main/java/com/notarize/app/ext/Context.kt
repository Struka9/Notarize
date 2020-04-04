package com.notarize.app.ext

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.text.NumberFormat
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.notarize.app.R
import java.io.File
import java.math.BigDecimal


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

fun Context.copyTextToClipboard(text: String?, description: String?, confirmMessage: String) {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData =
        ClipData.newPlainText(
            description,
            text
        )
    clipboardManager.setPrimaryClip(clipData)

    Toast.makeText(
        this,
        confirmMessage,
        Toast.LENGTH_LONG
    ).show()
}

fun Context.createDialog(layout: View) =
    AlertDialog.Builder(this).run {
        setView(layout)
        setCancelable(true)
    }.create().apply {
        setCanceledOnTouchOutside(true)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

fun Context.createDialog(@LayoutRes layout: Int) =
    AlertDialog.Builder(this).run {
        setView(layout)
        setCancelable(true)
    }.create().apply {
        setCanceledOnTouchOutside(true)
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

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun BigDecimal.asCurrency(symbol: String = "$"): String {
    // TODO: Dynamic currency
    return "$symbol ${NumberFormat.getInstance().format(this)}"
}

@Throws(WriterException::class)
fun String.toQrBitmap(): Bitmap {
    val multiformatWriter = MultiFormatWriter()
    val bitMatrix: BitMatrix = multiformatWriter.encode(
        this,
        BarcodeFormat.QR_CODE,
        300, 300
    )
    val barcodeEncoder = BarcodeEncoder()
    return barcodeEncoder.createBitmap(bitMatrix)
}

