package com.example.appchat.ui.chat

import com.example.appchat.data.model.MessageModel

interface ChatView {
    fun loadNewMessageSuccess(model: MessageModel)
    fun loadMessageSuccess(list: ArrayList<MessageModel>, isCheck: Boolean)
    fun loadNodeChildSuccess(node: String)
    fun nullNodeChild()
    fun loadTokenSuccess(token: String)
    fun nullOldMessage()
}