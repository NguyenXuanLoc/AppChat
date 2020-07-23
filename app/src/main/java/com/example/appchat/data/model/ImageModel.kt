package com.example.appchat.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ImageModel(var id: String = "", var idStatus: String = "", var url: String = "")