package com.example.appchat.ui.login

import com.example.appchat.data.model.UserModel
import com.google.firebase.auth.FirebaseUser

interface LoginResponse {
    fun updateUI(user: FirebaseUser)
    fun lackOfInformation()
    fun wrongInfo()
    fun getUser(userModel: UserModel)
}