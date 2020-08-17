package com.example.appchat.widget

import android.app.Activity
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appchat.R
import com.example.appchat.common.util.PermissionUtil
import com.example.appchat.ui.test.PhotoAdapter
import com.example.fcm.common.ext.gone
import com.example.fcm.common.ext.visible
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.layout_sent_photo.*

class DialogSendImage(var ctx: Activity) : BottomSheetDialog(ctx) {
    private val photos = ArrayList<String>()
    private val urlPicks by lazy { ArrayList<String>() }
    private val adapter by lazy {
        PhotoAdapter(
            photos,
            ctx,
            { addPhoto(it) },
            { removePhoto(it) })
    }
    private var v: SendPhotoListener? = null

    init {
        this.setContentView(R.layout.layout_sent_photo)
        rclPhoto.adapter = adapter
        rclPhoto.layoutManager = GridLayoutManager(ctx, 3, GridLayoutManager.VERTICAL, false)
        rclPhoto.setHasFixedSize(true)
        if (PermissionUtil.isGranted(
                ctx, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        ) {
            loadListGallery()
        }
        eventHandle()
    }

    private fun eventHandle() {
        imgCancel.setOnClickListener {
            urlPicks.clear()
            adapter.notifyDataSetChanged()
            dismiss()
        }
        imgSend.setOnClickListener {
            v?.sendListPhoto(urlPicks)
            adapter.notifyDataSetChanged()
            dismiss()
        }
    }

    private fun loadListGallery() {
        var list = ArrayList<String>()
        var uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        var projection =
            arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        var orderBy = MediaStore.Video.Media.DATE_TAKEN
        var cursor = ctx.contentResolver.query(uri, projection, null, null, "$orderBy DESC")
        var columnIndexData = cursor?.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)

        if (cursor != null) {
            while (cursor.moveToNext()) {
                var absolutePathOfImage = columnIndexData?.let { cursor.getString(it) }
                absolutePathOfImage?.let { list.add(it) }
            }
        }
        photos.addAll(list)
        adapter.notifyDataSetChanged()
    }

    private fun addPhoto(path: String) {
        urlPicks.add(path)
        if (urlPicks.size > 0) {
            imgSend.visible()
        } else {
            imgSend.gone()
        }
    }

    private fun removePhoto(path: String) {
        if (urlPicks.size > 0) {
            for (i in 0 until urlPicks.size) {
                if (urlPicks[i] == path) {
                    urlPicks.removeAt(i)
                }
            }
            if (urlPicks.size > 0) {
                imgSend.visible()
            } else {
                imgSend.gone()
            }
        }
    }

    fun setSendPhotoListener(listener: SendPhotoListener) {
        v = listener
    }

    interface SendPhotoListener {
        fun sendListPhoto(urls: ArrayList<String>)
    }
}