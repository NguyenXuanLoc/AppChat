package com.example.appchat.ui.status

import android.net.Uri

class StatusPresenter(statusView: StatusView) : StatusResponse {
    private val model = StatusModel(this)
    val v = statusView
    fun uploadImage(uri: Uri) {
        model.uploadImage(uri)
    }
}