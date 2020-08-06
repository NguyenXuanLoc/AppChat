package com.example.appchat.ui.chat

import com.example.appchat.data.model.MessageModel

class ChatPresenter(view: ChatView) : ChatResponse {
    val v = view
    val model = ChatModel(this)
    fun sentMessage(notChild: String, messageModel: MessageModel) {
        model.sendMessage(notChild, messageModel)
    }

    fun checkNodeChild(idSend: String, idReceiver: String) {
        model.checkNodeChild(idSend, idReceiver)
    }

    override fun loadNodeChildSuccess(node: String) {

    }

    override fun nullNodeChild() {
    }
}