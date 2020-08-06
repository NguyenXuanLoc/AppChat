package com.example.appchat.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class OptionModel(
    var id: String? = "",
    var urlImage: String? = "",
    var title: String? = "",
    var background: String? = ""
) {
    fun isSoul(): Boolean {
        return id == "0"
    }

    fun isVoice(): Boolean {
        return id == "1"
    }

    fun isMovice(): Boolean {
        return id == "2"
    }
}