package com.notarize.app.ext

import timber.log.Timber
import java.math.BigDecimal

fun String.isValidQrContent() =
    "^ethereum:0x[a-fA-Z0-9]{40}(\\?value=\\d+(\\.\\d+)?(e-?\\d+)?)?\$".toRegex()
        .matches(this)

fun String.getAddress(): String? {
    if (!isValidQrContent()) return null
    val parts = split(":")
    return parts[1].split("?")[0]
}

fun String.getWeiAmount(): BigDecimal? {
    if (!isValidQrContent()) return null
    val parts = split(":")[1].split("?")

    if (parts.size < 2) {
        return null
    }

    return try {
        BigDecimal(parts[1].removePrefix("value="))
    } catch (e: NumberFormatException) {
        Timber.e(e)
        null
    }
}