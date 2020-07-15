package com.example.appchat.ui.register

interface RegisterView {
    fun success()
    fun errorPass(notify: Int)
    fun error()
    fun lackOfInformation()
}