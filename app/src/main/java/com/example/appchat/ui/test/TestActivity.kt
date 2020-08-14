package com.example.appchat.ui.test

import android.graphics.drawable.Icon
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.appchat.R
import com.example.appchat.common.Key
import com.example.appchat.common.ext.setImageSimple
import com.example.appchat.data.model.GifModel
import com.example.appchat.ui.base.BaseActivity
import com.example.fcm.common.ext.toast
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.request.ImageRequest
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : BaseActivity() {

    override fun contentView(): Int {
        return R.layout.activity_test
    }

    override fun init() {
//        wvTest.settings.javaScriptEnabled = true
//        wvTest.loadUrl("https://media0.giphy.com/media/Y1uxb7aBNQIRAUfhaE/200.webp")
    }

    override fun eventHandle() {
        imgVoice.setOnClickListener { toast("O") }
//        addGif()
    }

    private fun uploadGif(model: GifModel) {
        var ref = FirebaseDatabase.getInstance().getReference(Key.GIF).push()
        var id = ref.key
        model.id = id.toString()
        ref.setValue(model)
    }

    private fun addGif() {
        var list = self.resources.getStringArray(R.array.gif)
        for (element in list) {
            var model = GifModel("", element.toString())
            uploadGif(model)
        }
    }

}



