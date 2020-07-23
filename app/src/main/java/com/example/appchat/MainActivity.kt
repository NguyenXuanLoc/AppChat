package com.example.appchat

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.appchat.ui.home.HomeActivity
import com.example.appchat.ui.login.LoginActivity
import com.example.fcm.common.ext.getUser
import com.example.fcm.common.ext.openActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
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