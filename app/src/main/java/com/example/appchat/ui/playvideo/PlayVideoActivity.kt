package com.example.appchat.ui.playvideo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.common.ext.setImage
import com.example.fcm.common.ext.gone
import com.example.fcm.common.ext.visible
import kotlinx.android.synthetic.main.activity_play_video.*


class PlayVideoActivity : AppCompatActivity() {
    lateinit var uri: String
    var check = true
    private lateinit var controller: MediaController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video)
        init()
        eventHandle()
    }

    private fun init() {
        controller = MediaController(this);
        controller.setMediaPlayer(playerView);
        playerView.setMediaController(controller);
        playerView.requestFocus()
    }

    private fun eventHandle() {
        var bundle = intent.getBundleExtra(Constant.URI)
        bundle?.let { uri = bundle.getString(Constant.URI).toString() }
        playerView.setVideoURI(Uri.parse(uri))
        playerView.start()
        imgApply.setOnClickListener {
            check = if (check) {
                imgApply.setImage(R.drawable.ic_tick_while)
                lblApply.visible()
                false
            } else {
                imgApply.setImage(R.drawable.btn_circle_white)
                lblApply.gone()
                true
            }
        }
        lblBack.setOnClickListener { finish() }
        lblApply.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra(Constant.URI, uri)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }


}