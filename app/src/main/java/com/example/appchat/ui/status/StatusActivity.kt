package com.example.appchat.ui.status

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appchat.R
import com.example.appchat.widget.DialogChooseImage

class StatusActivity : AppCompatActivity(), StatusView, DialogChooseImage.ImageChooserListener {
    private lateinit var dialogUploadImage: DialogChooseImage
    private lateinit var presenter: StatusPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)
        init()
        eventHandle()
    }

    private fun eventHandle() {

    }

    private fun init() {
        presenter = StatusPresenter(this)
        dialogUploadImage = DialogChooseImage(this)
    }

    override fun onClickCamera() {
        TODO("Not yet implemented")
    }

    override fun onClickAlbum() {
        TODO("Not yet implemented")
    }

    override fun onCancel() {
        TODO("Not yet implemented")
    }
}