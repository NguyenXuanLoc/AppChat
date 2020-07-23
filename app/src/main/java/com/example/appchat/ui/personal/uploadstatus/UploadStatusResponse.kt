package com.example.appchat.ui.personal.uploadstatus

interface UploadStatusResponse {
    fun getKeyStatus(idStatus: String)
    fun uploadSuccess()
}