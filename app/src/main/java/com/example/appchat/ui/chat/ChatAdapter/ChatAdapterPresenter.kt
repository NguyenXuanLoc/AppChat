package com.example.appchat.ui.chat.ChatAdapter

import com.example.appchat.data.model.ImageModel

class ChatAdapterPresenter(var view: ChatAdapterView) : ChatAdapterResponse {
    val v = view
    val model = ChatAdapterModel(this)

    fun loadPhotos(idMessage: String) {
        model.getPhoto(idMessage)
    }

    override fun loadPhotosSuccess(list: ArrayList<ImageModel>) {
        v.loadPhotosSuccess(list)
    }

    override fun loadPhotosSuccessNull() {
        v.loadPhotosSuccessNull()
    }


}