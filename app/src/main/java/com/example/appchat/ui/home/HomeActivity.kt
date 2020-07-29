package com.example.appchat.ui.home

import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.data.model.UserModel
import com.example.appchat.ui.base.BaseActivity
import com.example.appchat.ui.personal.FragPersonal
import com.example.fcm.common.ext.saveUser
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {

    override fun contentView(): Int {
        return R.layout.activity_home
    }

    override fun init() {
        pagerMain.adapter = MainStateAdapter(supportFragmentManager)
    }

    override fun eventHandle() {
        getExtra()
        supportFragmentManager.beginTransaction().add(R.id.mLayout, FragPersonal()).commit()
    }

    override fun getExtra() {
        var bundle = intent.getBundleExtra(Constant.USER)
        if (bundle != null) {
            var user: UserModel = bundle.getSerializable(Constant.USER) as UserModel
            saveUser(user)
        }

    }

}