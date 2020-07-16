package com.example.appchat.ui.status

import android.net.Uri

class StatusPresenter(statusView: StatusView) : StatusResponse {
    private val model = StatusModel(this)
    val v = statusView
    fun uploadImage(uris: ArrayList<Uri>) {
        for (i in 0 until uris.size)
            model.uploadImage(uris[i])
    }
}