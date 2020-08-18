package com.example.appchat.ui.test

import com.example.appchat.R
import com.example.appchat.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_test.*

@Suppress("DEPRECATION")
class TestActivty : BaseActivity() {
    //    private val dialog by lazy { DialogSendImage(self) }
    private val photos by lazy { ArrayList<String>() }

    override fun contentView(): Int {
        return R.layout.activity_test
    }

    override fun init() {
    }

    override fun eventHandle() {
        btnClick.setOnClickListener {
        }
    }
}




