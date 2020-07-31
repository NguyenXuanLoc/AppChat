package com.example.appchat

import android.os.Handler
import com.example.appchat.ui.base.BaseActivity
import com.example.appchat.ui.home.HomeActivity
import com.example.appchat.ui.login.LoginActivity
import com.example.appchat.ui.test.TestActivity
import com.example.fcm.common.ext.getUser
import com.example.fcm.common.ext.openActivity


class MainActivity : BaseActivity() {

    override fun contentView(): Int {
        return R.layout.activity_main
    }

    override fun init() {
        hideToolbarBase()
    }

    override fun eventHandle() {
//        openActivity(TestActivity::class.java)
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