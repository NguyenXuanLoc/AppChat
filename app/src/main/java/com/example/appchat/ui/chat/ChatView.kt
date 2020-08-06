package com.example.appchat.ui.chat

import com.example.appchat.data.model.MessageModel

interface ChatView {
    fun loadMoreSuccess(list: ArrayList<MessageModel>)
    fun loadMessageSuccess(list: ArrayList<MessageModel>)
    fun loadNodeChildSuccess(node: String)
    fun nullNodeChild()
}