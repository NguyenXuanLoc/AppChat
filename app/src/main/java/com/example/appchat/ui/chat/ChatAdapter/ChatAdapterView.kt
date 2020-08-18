package com.example.appchat.ui.chat.ChatAdapter

import com.example.appchat.data.model.ImageModel

interface ChatAdapterView {
    fun loadPhotosSuccess(list: ArrayList<ImageModel>)
    fun loadPhotosSuccessNull()
}