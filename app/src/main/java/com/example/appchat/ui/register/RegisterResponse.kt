package com.example.appchat.ui.register

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

interface RegisterResponse {
    fun success()
    fun error()

}