package com.example.appchat.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.data.UserModel
import com.example.fcm.common.ext.getUser
import com.example.fcm.common.ext.saveUser
import com.example.fcm.common.ext.toast
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        init()
        evenHandle()
    }

    private fun init() {
        pagerMain.adapter = MainStateAdapter(supportFragmentManager)
        toast(getUser()?.userName.toString())
    }

    private fun evenHandle() {
        getExtra()
    }

    private fun getExtra() {
        var bundle = intent.getBundleExtra(Constant.USER)
        if (bundle != null) {
            var user: UserModel = bundle.getSerializable(Constant.USER) as UserModel
            saveUser(user)
        }

    }

}