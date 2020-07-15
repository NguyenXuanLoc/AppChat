package com.example.appchat.widget

import android.content.Context
import com.example.appchat.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.layout_choose_image.*
import timber.log.Timber

class DialogChooseImage(ctx: Context) : BottomSheetDialog(ctx, R.style.BottomSheepDialogTheme) {
    private var listener: ImageChooserListener? = null

    init {
        this.setContentView(R.layout.layout_choose_image)
        btnCamera.setOnClickListener {
            this.dismiss()
            listener?.onClickCamera()
            Timber.e("Camera")
        }
        btnPick.setOnClickListener {
            listener?.onClickAlbum()
            Timber.e("Upload")
        }
        btnCancel.setOnClickListener {
            listener?.onCancel()
            Timber.e("Upload")
        }
    }

    fun setImageChooserListener(listener: ImageChooserListener) {
        this.listener = listener
    }

    interface ImageChooserListener {
        fun onClickCamera()

        fun onClickAlbum()

        fun onCancel()
    }
}