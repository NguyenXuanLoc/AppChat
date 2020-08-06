package com.example.appchat.widget

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import com.example.appchat.R
import com.example.appchat.common.KeyPermission
import com.example.appchat.common.util.FileUtil
import com.example.appchat.common.util.PermissionUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.layout_choose_image.*

class DialogChooseImage(ctx: Context) : BottomSheetDialog(ctx, R.style.BottomSheepDialogTheme) {
    private var listener: ImageChooserListener? = null

    init {
        this.setContentView(R.layout.layout_choose_image)
        btnCamera.setOnClickListener {
            if (PermissionUtil.isGranted(
                    ctx,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    KeyPermission.RC_PERMISSION_WRITE_EXTERNAL_STORAGE
                )
            ) {
                FileUtil.openCamera(ctx as Activity)
            }
            this.dismiss()
        }

        btnAlbum.setOnClickListener {
            if (PermissionUtil.isGranted(
                    ctx,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    KeyPermission.RC_PERMISSION_READ_EXTERNAL_STORAGE
                )
            ) {
                FileUtil.openStorage(ctx as Activity)
            }
            this.dismiss()
        }
        btnCancel.setOnClickListener {
            this.dismiss()
        }
    }

    fun setImageChooserListener(listener: ImageChooserListener) {
        this.listener = listener
    }

    interface ImageChooserListener {

    }
}