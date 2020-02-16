package com.tallycheck.app.ext

import java.math.BigInteger
import java.security.MessageDigest

fun ByteArray.toSha256(): ByteArray {
    val md = MessageDigest.getInstance("sha256")
    return md.digest(this)
}

fun ByteArray.toHexString(): String {
    val bi = BigInteger(1, this)
    val hexString = StringBuilder(bi.toString(16))
    // Pad with leading zeros
    while (hexString.length < 32) {
        hexString.insert(0, '0')
    }
    return hexString.toString()
}