package com.example.appchat.ui.fcm

import com.example.appchat.common.Constant

data class Data(
    var title: String,
    var message: String,
    var idSender: String,
    var options: String = Constant.MESSAGE
) {
}