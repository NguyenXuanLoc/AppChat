package com.example.appchat

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.appchat.ui.base.BaseActivity
import com.example.appchat.ui.home.HomeActivity
import com.example.appchat.ui.login.LoginActivity
import com.example.appchat.ui.test.test
import com.example.fcm.common.ext.getUser
import com.example.fcm.common.ext.openActivity
import com.example.fcm.common.ext.removeUser


class MainActivity : BaseActivity() {

    override fun contentView(): Int {
        return R.layout.activity_main
    }

    override fun init() {

    }

    override fun eventHandle() {
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