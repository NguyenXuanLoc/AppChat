package com.example.appchat.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class VideoModel(var id: String = "", var idStatus: String = "", var url: String = "")