package com.notarize.app.ext

import java.text.SimpleDateFormat
import java.util.*

val simpleTimeFormat = SimpleDateFormat("dd MM, yyyy hh:mm", Locale.getDefault())

fun Calendar.format(): String = simpleTimeFormat.format(time)