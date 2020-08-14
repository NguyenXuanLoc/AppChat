package com.example.appchat.ui.chat

import com.example.appchat.data.model.GifModel
import com.example.appchat.data.model.MessageModel
import com.example.appchat.data.model.UserModel

class ChatPresenter(view: ChatView) : ChatResponse {
    val v = view
    val model = ChatModel(this)

    fun loadOldMessage(node: String, topNode: String) {
        model.loadOldMessage(node, topNode)
    }

    fun loadNewMessage(node: String) {
        model.loadNewMessage(node)
    }

    fun loadGif() {
        model.loadNewGift()
    }

    fun loadMoreGif(lastNode: String) {
        model.loadMoreGift(lastNode)
    }

    fun sentMessage(notChild: String, messageModel: MessageModel, userReceive: UserModel) {
        model.sendMessage(notChild, messageModel, userReceive)
    }

    fun checkNodeChild(idSend: String, idReceiver: String) {
        model.checkNodeChild(idSend, idReceiver)
    }


    //Check Status online or offline to push notification
    fun getTokenAndCheckStatus(userModel: UserModel) {
        model.getToken(userModel)
        model.checkStatus(userModel)
    }


    override fun loadNewMessageSuccess(model: MessageModel) {
        v.loadNewMessageSuccess(model)
    }

    override fun loadMessageSuccess(list: ArrayList<MessageModel>, isCheck: Boolean) {
        v.loadMessageSuccess(list, isCheck)
    }

    override fun loadNodeChildSuccess(node: String) {
        v.loadNodeChildSuccess(node)
    }

    override fun nullNodeChild() {
        v.nullNodeChild()
    }

    override fun nullOldMessage() {
        v.nullOldMessage()
    }

    override fun loadTokenSuccess(token: String) {
        v.loadTokenSuccess(token)
    }

    override fun loadGifSuccess(list: ArrayList<GifModel>) {
        v.loadGifSuccess(list)
    }

    override fun loadMoreGifSuccess(list: ArrayList<GifModel>) {
        v.loadMoreGifSuccess(list)
    }

}