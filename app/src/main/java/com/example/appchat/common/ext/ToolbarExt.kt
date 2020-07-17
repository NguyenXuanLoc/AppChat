package com.example.appchat.common.ext

import android.os.Build
import android.widget.Toolbar
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Toolbar.removeTitle() {
    title = ""
}