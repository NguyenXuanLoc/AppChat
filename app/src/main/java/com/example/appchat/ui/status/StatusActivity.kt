package com.example.appchat.ui.status

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.common.util.FileUtil
import com.example.appchat.widget.DialogChooseImage
import com.example.fcm.common.ext.toast
import kotlinx.android.synthetic.main.activity_status.*


class StatusActivity : AppCompatActivity(), StatusView, DialogChooseImage.ImageChooserListener {
    companion object {
        private const val REQUEST_EXTERNAL_STORAGE = 1
    }


    private lateinit var dialogUploadImage: DialogChooseImage
    private lateinit var presenter: StatusPresenter
    private lateinit var listUri: ArrayList<Uri>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)
        init()
        eventHandle()
    }

    private fun init() {
        presenter = StatusPresenter(this)
        dialogUploadImage = DialogChooseImage(this)
        dialogUploadImage.setImageChooserListener(this)
        listUri = ArrayList()
    }

    private fun eventHandle() {
        layoutImage.setOnClickListener { dialogUploadImage.show() }
    }

    override fun onClickCamera() {
        FileUtil.openCamera(this)

    }

    override fun onClickAlbum() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FileUtil.checkPermissions(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                Constant.GET_IMAGE -> {
                    val clipData: ClipData? = data.clipData
                    if (clipData != null) {
                        for (i in 0 until clipData.itemCount) listUri.add(clipData.getItemAt(i).uri)
                    } else {
                        data.data?.let { listUri.add(it) }
                    }
                }
                Constant.OPEN_CAMERA -> {
                    val bitmap = data.extras!!["data"] as Bitmap?
                    val uri = FileUtil.getImageUri(this, bitmap)
                    uri?.let { listUri.add(it) }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] === PackageManager.PERMISSION_GRANTED
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        FileUtil.openStorage(this)
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }
    }
}