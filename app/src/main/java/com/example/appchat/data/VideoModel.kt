package com.example.appchat.data

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
class VideoModel(id: String, idStatus: String, url: String) : Serializable