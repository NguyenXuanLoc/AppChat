package com.example.appchat.ui.home

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appchat.R
import com.example.appchat.common.Key
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
        toast(getUser(getPreferences(Context.MODE_PRIVATE))?.userName.toString())
    }

    private fun evenHandle() {
        getExtra()
    }

    private fun getExtra() {
        var bundle = intent.getBundleExtra(Key.USER)
        if (bundle != null) {
            var user: UserModel = bundle.getSerializable(Key.USER) as UserModel
            saveUser(getSharedPreferences(Key.USER, Context.MODE_PRIVATE), user)
        }

    }

}