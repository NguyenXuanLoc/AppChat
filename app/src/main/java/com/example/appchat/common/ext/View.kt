package com.example.fcm.common.ext

import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fcm.common.util.CommonUtil

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}
fun View.setRatio(
    activity: AppCompatActivity,
    x: Int,
    y: Int,
    margin: Int,
    width: Int = CommonUtil.getScreenWidthAsPixel(activity)
) {
    val w = width - margin
    layoutParams.width = w
    layoutParams.height = w * y / x
}