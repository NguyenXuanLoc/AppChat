package com.example.appchat.data

import java.io.Serializable

class UserModel() : Serializable {
    var age: String? = null
    var id: String? = null
    var imageUrl: String? = null
    var sex: String? = null
    var status: String? = null
    var userName: String? = null

    constructor(
        age: String,
        id: String?,
        imageUrl: String?,
        sex: String?,
        status: String?,
        userName: String?
    ) : this() {
        this.age = age
        this.id = id
        this.imageUrl = imageUrl
        this.sex = sex
        this.status = status
        this.userName = userName
    }
}