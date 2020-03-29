package com.notarize.app.ext

import java.math.BigDecimal

fun BigDecimal?.isNullOrZero(): Boolean {
    return this == null || compareTo(BigDecimal.ZERO) == 0
}