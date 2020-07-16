package com.example.appchat.common.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.appchat.common.Constant
import java.io.ByteArrayOutputStream


object FileUtil {
    @RequiresApi(Build.VERSION_CODES.M)
    fun checkPermissions(activity: Activity) {
        if (activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity, Array(1) { Manifest.permission.READ_EXTERNAL_STORAGE },
                Constant.REQUEST_EXTERNAL_STORAGE
            )
            return;
        } else {
            openStorage(activity)
        }

    }

    fun openStorage(activity: Activity) {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.action = Intent.ACTION_GET_CONTENT
        activity.startActivityForResult(intent, Constant.GET_IMAGE)
    }

    fun openCamera(activity: Activity) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        activity.startActivityForResult(intent, Constant.OPEN_CAMERA)
    }
    fun getImageUri(inContext: Context, inImage: Bitmap?): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }
}