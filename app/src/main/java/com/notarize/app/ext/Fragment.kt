package com.notarize.app.ext

import android.content.Intent
import android.net.Uri
import android.webkit.URLUtil
import androidx.fragment.app.Fragment
import com.notarize.app.R

fun Fragment.viewWebpage(url: String) {
    if (!URLUtil.isValidUrl(url)) return
    context?.startActivity(Intent.createChooser(Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
    }, getString(R.string.view)))
}
