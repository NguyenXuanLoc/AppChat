package com.example.appchat.ui.test

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.widget.DialogChooseImage
import com.example.fcm.common.ext.toast
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_test.*
import timber.log.Timber


class test : AppCompatActivity(), DialogChooseImage.ImageChooserListener {
    lateinit var dialogChooseImage: DialogChooseImage
    private lateinit var uri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        init()
    }

    private fun init() {
        var bundle = intent.getBundleExtra(Constant.PLAY_VIDEO)
        if (bundle != null) {
            var uri = bundle.getString(Constant.PLAY_VIDEO)
        }
    }

}