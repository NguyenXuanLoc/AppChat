package com.example.appchat.ui.main

import android.media.MediaPlayer
import android.os.Handler
import android.util.Log
import com.example.appchat.R
import com.example.appchat.ui.base.BaseActivity
import com.example.appchat.ui.chat.ChatActivity
import com.example.appchat.ui.home.HomeActivity
import com.example.appchat.ui.login.LoginActivity
import com.example.appchat.ui.test.Test2Activity
import com.example.appchat.ui.test.TestActivty
import com.example.appchat.ui.uploadstatus.UploadStatusActivity
import com.example.appchat.ui.voicecall.VoiceCallActivity
import com.example.fcm.common.ext.getUser
import com.example.fcm.common.ext.openActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import java.lang.Exception


class MainActivity : BaseActivity() {

    override fun contentView(): Int {
        return R.layout.activity_main
    }

    override fun init() {
        hideToolbarBase()
    }

    override fun eventHandle() {
        //  removeUser()
//        openActivity(UploadStatusActivity::class.java)
        Handler().postDelayed(Runnable {
            if (getUser() != null) {
                openActivity(HomeActivity::class.java)
            } else openActivity(LoginActivity::class.java)
        }, 1000)
    }


    override fun onStop() {
        super.onStop()
        finish()
    }

}