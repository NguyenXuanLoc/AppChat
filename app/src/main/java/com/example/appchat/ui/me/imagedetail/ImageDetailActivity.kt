package com.example.appchat.ui.me.imagedetail

import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.core.graphics.drawable.toBitmap
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.data.model.ImageModel
import com.example.appchat.ui.base.BaseActivity
import com.example.fcm.common.ext.toast
import kotlinx.android.synthetic.main.activity_image_info.*
import kotlinx.android.synthetic.main.item_image_detail.*


class ImageDetailActivity : BaseActivity() {
    private val images by lazy { ArrayList<ImageModel>() }
    private val adapter by lazy { ImageDetailAdapter(this, images) }
    private var position: Int? = null

    override fun contentView(): Int {
        return R.layout.activity_image_info
    }

    override fun eventHandle() {
        hideToolbarBase()
        //Listener
        imgDownload.setOnClickListener {
            var nameFile = images[vpImage.currentItem].url
            sdvTest.setImageURI(nameFile)
            saveImageToStorage(sdvTest.drawable.toBitmap(sdvTest.width, sdvTest.height))
        }
    }

    override fun init() {
        vpImage.adapter = adapter
        vpImage.setPageTransformer(true) { page, position -> page.rotationY = (position * -10) }
        adapter.notifyDataSetChanged()
        position?.let { vpImage.currentItem = it }
    }

    override fun getExtra() {
        var bundle = intent.getBundleExtra(Constant.INFO_IMAGE)
        if (bundle != null) {
            var results = bundle.getSerializable(Constant.INFO_IMAGE) as ArrayList<ImageModel>
            images.addAll(results)
            adapter.notifyDataSetChanged()
            position = bundle.getInt(Constant.POSITION)

        }
    }


    private fun saveImageToStorage(bitmap: Bitmap) {
        MediaStore.Images.Media.insertImage(contentResolver, bitmap, "", "")
        toast(getString(R.string.download_image_success))
    }
}