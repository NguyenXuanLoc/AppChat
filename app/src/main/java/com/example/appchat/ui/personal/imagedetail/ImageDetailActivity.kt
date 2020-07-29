package com.example.appchat.ui.personal.imagedetail

import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.data.model.ImageModel
import com.example.fcm.common.ext.toast
import kotlinx.android.synthetic.main.activity_image_info.*
import kotlinx.android.synthetic.main.item_image_detail.*
import java.io.FileOutputStream


class ImageDetailActivity : AppCompatActivity() {
    private val images by lazy { ArrayList<ImageModel>() }
    private val adapter by lazy { ImageDetailAdapter(this, images) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_info)
        init()
        getExtra()
        eventHandle()
    }

    private fun eventHandle() {
        //Listener
        imgDownload.setOnClickListener {
            var nameFile = images[vpImage.currentItem].url
            sdvTest.setImageURI(nameFile)
            saveImageToStorage(sdvTest.drawable.toBitmap(sdvTest.width, sdvTest.height))
        }
    }

    private fun init() {
        vpImage.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun getExtra() {
        var bundle = intent.getBundleExtra(Constant.INFO_IMAGE)
        if (bundle != null) {
            var results = bundle.getSerializable(Constant.INFO_IMAGE) as ArrayList<ImageModel>
            images.addAll(results)
            adapter.notifyDataSetChanged()
            var position = bundle.getInt(Constant.POSITION)
            vpImage.currentItem = position
        }
    }


    private fun saveImageToStorage(bitmap: Bitmap) {
        MediaStore.Images.Media.insertImage(contentResolver, bitmap, "", "")
        toast(getString(R.string.download_image_success))
    }
}