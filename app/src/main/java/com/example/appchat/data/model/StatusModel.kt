package com.example.appchat.data.model

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
class StatusModel(
    var id: String = "",
    var idUser: String = "",
    var status: String = "",
    var like: String = "",
    var time: String = "",
    var date: String = "",
    var attach: String = "",
    var video: String = "",
    var thumbnail: String = "",
    var audio: String = "",
    var duration: String = ""
) : Serializable
