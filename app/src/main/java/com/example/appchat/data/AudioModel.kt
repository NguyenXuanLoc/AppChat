package com.example.appchat.data

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class AudioModel(
    var id: String = "",
    var idStatus: String = "",
    var url: String = ""
) : Serializable {

}