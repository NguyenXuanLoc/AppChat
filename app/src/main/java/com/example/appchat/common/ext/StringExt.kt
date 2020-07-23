package com.example.fcm.common.ext

import java.text.SimpleDateFormat
import java.util.*

fun String.removeChar(str: String, n: Int): String? {
    val front = str.substring(0, n)
    val back = str.substring(n + 1, str.length)
    return front + back
}

fun String.currentTime(): String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
