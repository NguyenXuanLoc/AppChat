package com.example.fcm.common.ext

fun String.removeChar(str: String, n: Int): String? {
    val front = str.substring(0, n)
    val back = str.substring(n + 1, str.length)
    return front + back
}
