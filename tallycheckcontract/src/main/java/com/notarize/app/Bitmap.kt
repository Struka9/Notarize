package com.notarize.app

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

fun Bitmap.toByteArray(): ByteArray {
    val bso = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, 70, bso)

    return bso.toByteArray()
}