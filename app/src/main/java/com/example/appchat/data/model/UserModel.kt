package com.example.appchat.data.model

import com.example.appchat.common.Key
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
class UserModel(
    var id: String? = "",
    var userName: String? = "",
    var dateOfBirth: String? = "",
    var phoneNumber: String? = "",
    var imageUrl: String? = "",
    var gender: String? = "",
    var story: String? = "",
    var status: String? = ""
) : Serializable {
    fun isMale(): Boolean {
        return gender == Key.MALE
    }
    fun isFeMale(): Boolean {
        return gender == Key.FEMALE
    }
}
