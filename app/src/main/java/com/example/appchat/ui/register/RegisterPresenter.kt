package com.example.appchat.ui.register

import com.example.appchat.R
import com.google.firebase.auth.FirebaseAuth

class RegisterPresenter(registerView: RegisterView) : RegisterResponse {
    var model: RegisterModel = RegisterModel(this)
    var v = registerView

    fun registerAccount(auth: FirebaseAuth, email: String, pass: String, confirmPass: String) {
        if (email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) v.lackOfInformation()
        else {

            if (pass == confirmPass) {
                if (pass.length < 6) v.errorPass(R.string.wrong_pass)
                else model.registerAccount(auth, email, pass)
            } else v.errorPass(R.string.not_equals_pass)
        }

    }

    override fun success() {
        v.success()
    }

    override fun error() {
        v.error()
    }

}