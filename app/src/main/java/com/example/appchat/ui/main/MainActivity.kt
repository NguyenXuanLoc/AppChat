package com.example.appchat.ui.main

import android.content.Context
import android.os.Handler
import android.util.Log
import com.example.appchat.R
import com.example.appchat.common.Key
import com.example.appchat.data.model.UserModel
import com.example.appchat.ui.base.BaseActivity
import com.example.appchat.ui.home.HomeActivity
import com.example.appchat.ui.login.LoginActivity
import com.example.fcm.common.ext.getUser
import com.example.fcm.common.ext.openActivity
import com.example.fcm.common.ext.removeUser
import com.example.fcm.common.ext.toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


class MainActivity : BaseActivity() {

    override fun contentView(): Int {
        return R.layout.activity_main
    }

    override fun init() {
        hideToolbarBase()

    }

    override fun eventHandle() {
//        removeUser()
//        openActivity(MessagingActivity::class.java)
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