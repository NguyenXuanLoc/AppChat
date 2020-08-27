package com.example.appchat.ui.chat

import android.net.Uri
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

    // Send attach audio,photos
    fun sendAttach(
        nodeChild: String,
        useSend: UserModel,
        urlPhotos: ArrayList<String>? = null,
        messageModel: MessageModel,
        uriAudio: Uri? = null
    ) {
        urlPhotos?.let {
            model.sendMessage(nodeChild, messageModel, useSend, it)
        }
        uriAudio?.let {
            model.sendMessage(nodeChild, messageModel, useSend, uriAudio = it)
        }
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

    override fun loadOldMessageSuccess(list: ArrayList<MessageModel>) {
        v.loadOldMessageSuccess(list)
    }

    override fun loadFirstMessageSuccess(list: ArrayList<MessageModel>) {
        v.loadFirstMessageSuccess(list)
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

    fun pushNotifyVoiceCall(
        userToken: String,
        nameSender: String,
        message: String,
        idSender: String
    ) {
        model.pushNotifyCallVideo(userToken, nameSender, message, idSender)
    }

}