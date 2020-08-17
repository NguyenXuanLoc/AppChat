package com.example.appchat.ui.test

import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toUri
import com.example.appchat.R
import com.example.appchat.common.Key
import com.example.appchat.common.util.PermissionUtil
import com.example.appchat.widget.DialogSendImage
import com.example.appchat.ui.base.BaseActivity
import com.example.fcm.common.ext.toast
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_test.*
import java.io.File

@Suppress("DEPRECATION")
class TestActivty : BaseActivity(), DialogSendImage.SendPhotoListener {
    private val dialog by lazy { DialogSendImage(self) }
    private val photos by lazy { ArrayList<String>() }

    override fun contentView(): Int {
        return R.layout.activity_test
    }

    override fun init() {
        dialog.setSendPhotoListener(this)
    }

    override fun eventHandle() {
        btnClick.setOnClickListener {
            dialog.show()
        }
    }

    override fun sendListPhoto(urls: ArrayList<String>) {
        toast(urls.size.toString())
        uploadFile(urls)
    }


    private fun uploadFile(urlPicks: ArrayList<String>) {
        for (i in 0 until urlPicks.size) {
            var uri = Uri.fromFile(File(urlPicks[i]))
            var mStorageRef = FirebaseStorage.getInstance().reference
            var nameFile = System.currentTimeMillis().toString()
            mStorageRef.child(Key.IMAGE).child(nameFile).putFile(uri).addOnSuccessListener { it ->
                var result = it.metadata?.reference?.downloadUrl
                result?.addOnSuccessListener {
                    Log.e("TAG", it.toString())

                }
            }
        }
    }
}



