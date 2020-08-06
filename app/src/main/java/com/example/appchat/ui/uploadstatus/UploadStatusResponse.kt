package com.example.appchat.ui.uploadstatus

interface UploadStatusResponse {
    fun getKeyStatus(idStatus: String)
    fun uploadSuccess()
}