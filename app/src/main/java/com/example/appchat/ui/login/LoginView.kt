package com.example.appchat.ui.login

import com.example.appchat.data.UserModel
import com.google.firebase.auth.FirebaseUser

interface LoginView {
    fun checkEmail()
    fun lackInfo()
    fun error()
    fun wrongInfo()
    fun lackOfInformation()
    fun updateUI(user: FirebaseUser)
    fun getUser(user: UserModel)
}