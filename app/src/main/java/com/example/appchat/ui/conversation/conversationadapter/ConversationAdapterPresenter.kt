package com.example.appchat.ui.conversation.conversationadapter

import com.example.appchat.data.model.MessageModel

class ConversationAdapterPresenter(view: ConversationAdapterView) : ConversationAdapterResponse {
    val v = view
    val model = ConversationAdapterModel(this)
    fun checkNodeChild(idReceive: String, idSend: String) {
        model.checkNodeChild(idSend, idReceive)
    }


    override fun resultLastMessage(model: MessageModel) {
        v.resultLastMessage(model)
    }

    override fun resultCountUnread(count: String) {
        v.resultCountUnread(count)
    }
}