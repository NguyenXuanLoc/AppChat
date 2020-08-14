package com.example.appchat.ui.chat

import com.example.appchat.data.model.GifModel
import com.example.appchat.data.model.MessageModel

interface ChatResponse {
    // if isCheck =true : load First message, isCheck=false: load more
    fun loadMessageSuccess(list: ArrayList<MessageModel>, isCheck: Boolean)
    fun loadNewMessageSuccess(model: MessageModel)
    fun loadNodeChildSuccess(node: String)
    fun nullNodeChild()
    fun nullOldMessage()
    fun loadTokenSuccess(token: String)
    fun loadGifSuccess(list: ArrayList<GifModel>)
    fun loadMoreGifSuccess(list: ArrayList<GifModel>)
}