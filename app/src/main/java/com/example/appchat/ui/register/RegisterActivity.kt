package com.example.appchat.ui.register

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.appchat.R
import com.example.appchat.ui.base.BaseActivity
import com.example.fcm.common.ext.invisible
import com.example.fcm.common.ext.toast
import com.example.fcm.common.ext.visible
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity(), RegisterView {
    private lateinit var auth: FirebaseAuth
    private lateinit var presenter: RegisterPresenter
    private lateinit var btnRegister: TextView


    override fun contentView(): Int {
        return R.layout.activity_register
    }

    override fun eventHandle() {
        btnRegister.setOnClickListener {
            pbLoading.visible()
            presenter.registerAccount(
                auth,
                tilEmail.editText?.text.toString(),
                tilPass.editText?.text.toString(),
                tilConfirmPass.editText?.text.toString()
            )
        }
    }

    override fun init() {
        btnRegister = findViewById(R.id.btnRegister)
        auth = FirebaseAuth.getInstance()
        presenter = RegisterPresenter(this)
    }

    override fun success() {
        pbLoading.invisible()
        toast(getString(R.string.register_success))
        finish()
    }

    override fun errorPass(notify: Int) {
        pbLoading.invisible()
        toast(getString(notify))
    }

    override fun error() {
        pbLoading.invisible()
        toast(getString(R.string.account_exits))
    }

    override fun lackOfInformation() {
        pbLoading.invisible()
        toast(getString(R.string.lack_of_information))
    }


}