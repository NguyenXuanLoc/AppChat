package com.example.appchat.data.model

import com.example.appchat.common.Key
import com.google.firebase.database.IgnoreExtraProperties
import java.text.SimpleDateFormat
import java.util.*

@IgnoreExtraProperties
data class CallModel(
    var id: String,
    var idSender: String,
    var idReceive: String,
    var status: String,
    var date: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
)