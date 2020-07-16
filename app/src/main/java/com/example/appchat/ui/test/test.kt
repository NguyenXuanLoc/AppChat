package com.example.appchat.ui.test

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.appchat.R
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
//        chooseImage()
//        test()
        test2()
    }

    private fun init() {
        dialogChooseImage = DialogChooseImage(this)
        dialogChooseImage.setImageChooserListener(this)
    }

    fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            uri = data.data!!

        }
    }

    fun test() {
        btnUpload.setOnClickListener {
            var mStorageRef: StorageReference = FirebaseStorage.getInstance().reference;
            val file: Uri = uri
            val riversRef: StorageReference =
                mStorageRef.child(System.currentTimeMillis().toString())
            riversRef.putFile(file)
                .addOnSuccessListener {   // Get a URL to the uploaded content
                    toast(it.toString())
                }
                .addOnFailureListener {
                    Timber.e(it.message)
                    // Handle unsuccessful uploads
                    // ...
                }
        }
    }

    fun test2() {
        btnUpload.setOnClickListener { dialogChooseImage.show() }
    }

    override fun onClickCamera() {
        toast("camera")
    }

    override fun onClickAlbum() {
        toast("camera")
    }

}