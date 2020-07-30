package com.example.appchat.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appchat.R
import com.example.appchat.ui.base.BaseFragment
import com.example.fcm.common.ext.toast


class FragTest : BaseFragment() {

    override fun eventHandle() {
    }

    override fun init() {
        toast("OK")
    }

    override fun onCreateView(): Int {
        return R.layout.activity_register
    }


}