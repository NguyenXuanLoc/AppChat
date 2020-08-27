package com.example.appchat.ui.chat

import com.example.appchat.data.model.GifModel
import com.example.appchat.data.model.MessageModel

interface ChatView {
    fun loadOldMessageSuccess(list: ArrayList<MessageModel>)
    fun loadNewMessageSuccess(model: MessageModel)
    fun loadFirstMessageSuccess(list: ArrayList<MessageModel>)
    fun loadNodeChildSuccess(node: String)
    fun nullNodeChild()
    fun loadTokenSuccess(token: String)
    fun nullOldMessage()
    fun loadGifSuccess(list: ArrayList<GifModel>)
    fun loadMoreGifSuccess(list: ArrayList<GifModel>)

}