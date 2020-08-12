package com.example.appchat.ui.chat

import com.example.appchat.data.model.MessageModel
import com.example.appchat.data.model.UserModel

class ChatPresenter(view: ChatView) : ChatResponse {
    val v = view
    val model = ChatModel(this)
    fun sentMessage(notChild: String, messageModel: MessageModel, userReceive: UserModel) {
        model.sendMessage(notChild, messageModel, userReceive)
    }

    fun checkNodeChild(idSend: String, idReceiver: String) {
        model.checkNodeChild(idSend, idReceiver)
    }

    fun loadMore(node: String, lastNode: String, isNew: Boolean = true) {
        model.loadMoreMessage(node, lastNode, isNew)
    }

    //Check Status online or offline to push notification
    fun getTokenAndCheckStatus(userModel: UserModel) {
        model.getToken(userModel)
        model.checkStatus(userModel)
    }

    override fun loadNewMessageSuccess(list: ArrayList<MessageModel>) {
        v.loadNewMessageSuccess(list)
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

    override fun loadTokenSuccess(token: String) {
        v.loadTokenSuccess(token)
    }

}