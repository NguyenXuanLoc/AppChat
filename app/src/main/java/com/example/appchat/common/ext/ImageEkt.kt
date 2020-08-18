@file:Suppress("DEPRECATION")

package com.example.appchat.common.ext

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.appchat.R
import com.facebook.drawee.view.SimpleDraweeView
import java.io.File


fun SimpleDraweeView.setImageSimple(
    src: Any?,
    ctx: Activity,
    errorImage: Int? = R.drawable.img_circle_letter
) {
    hierarchy?.setFailureImage(errorImage!!)
    when (src) {
        is String -> {
            if (src.isNotEmpty()) {
                setImageURI(src)
            } else {
                setActualImageResource(errorImage!!)
            }
        }
        is Int -> {
            setActualImageResource(src)
        }
        is Uri -> {
            setImageURI(src, ctx)
        }
        is File -> {
            setImageURI(Uri.fromFile(src), ctx)
        }
        is Bitmap ->{
            setImageBitmap(src)
        }
        else -> setActualImageResource(errorImage!!)
    }
}

fun ImageView.setImage(src: Any, ctx: Activity, errorImage: Int? = R.drawable.img_circle_letter) {
    when (src) {
        is Int -> {
            setImageDrawable(ctx.getDrawable(src))
        }
        is Uri -> {
            setImageURI(src)
        }
        is String -> {
            if (src.isNotEmpty())
                Glide.with(ctx).load(src).into(this)
            else setImageResource(errorImage!!)
        }
    }
}