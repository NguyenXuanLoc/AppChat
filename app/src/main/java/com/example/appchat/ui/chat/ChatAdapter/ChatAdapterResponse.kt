package com.example.appchat.ui.chat.ChatAdapter

import com.example.appchat.data.model.ImageModel

interface ChatAdapterResponse {
    fun loadPhotosSuccess(list: ArrayList<ImageModel>)
    fun loadPhotosSuccessNull()
}