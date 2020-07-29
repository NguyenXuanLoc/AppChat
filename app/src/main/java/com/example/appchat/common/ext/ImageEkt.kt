package com.example.appchat.common.ext

import android.app.Activity
import android.net.Uri
import android.widget.ImageView
import com.example.appchat.R
import com.facebook.drawee.view.SimpleDraweeView
import java.io.File

fun ImageView.setImage(image: Int) {
    this.setImageResource(image)
}

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
        else -> setActualImageResource(errorImage!!)
    }
}