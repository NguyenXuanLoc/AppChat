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

    fun loadMore(node: String, lastNode: String) {
        model.loadMoreMessage(node, lastNode)
    }

    override fun loadMoreSuccess(list: ArrayList<MessageModel>) {
        v.loadMoreSuccess(list)
    }

    override fun loadMessageSuccess(list: ArrayList<MessageModel>) {
        v.loadMessageSuccess(list)
    }

    override fun loadNodeChildSuccess(node: String) {
        v.loadNodeChildSuccess(node)
    }

    override fun nullNodeChild() {
        v.nullNodeChild()
    }
}