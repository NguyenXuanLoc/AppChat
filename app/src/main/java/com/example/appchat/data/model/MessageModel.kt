package com.example.appchat.data.model

import com.example.appchat.common.Key
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class MessageModel(
    var id: String? = "",
    var sender: String? = "",
    var received: String? = "",
    var message: String? = "",
    var urlAudio: String? = "",
    var urlVideo: String? = "",
    var thumbnail: String? = "",
    var icon: String? = "",
    var time: String? = "",
    var isSend: String? = Key.SENDING
) : Serializable