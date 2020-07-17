package com.example.appchat.widget

import android.app.Activity
import android.content.Context
import android.os.Build
import com.example.appchat.R
import com.example.appchat.common.util.FileUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.layout_choose_image.*

class DialogChooseImage(ctx: Context) : BottomSheetDialog(ctx, R.style.BottomSheepDialogTheme) {
    private var listener: ImageChooserListener? = null

    init {
        this.setContentView(R.layout.layout_choose_image)
        btnCamera.setOnClickListener {
            this.dismiss()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                FileUtil.checkPermissions(ctx as Activity)
            }
            FileUtil.openCamera(ctx as Activity)
        }

        btnAlbum.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                FileUtil.checkPermissions(ctx as Activity)
            }
            this.dismiss()
            FileUtil.openStorage(ctx as Activity)
        }
        btnCancel.setOnClickListener {
            this.dismiss()
        }
    }

    fun setImageChooserListener(listener: ImageChooserListener) {
        this.listener = listener
    }

    interface ImageChooserListener {
/*        fun onClickCamera()
        fun onClickAlbum()*/
    }
}