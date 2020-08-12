package com.example.appchat.ui.conversation.conversationadapter

class ConversationAdapterPresenter(view: ConversationAdapterView) : ConversationAdapterResponse {
    val v = view
    val model = ConversationAdapterModel(this)
    fun checkNodeChild(idSend: String, idReceive: String) {
        model.checkNodeChild(idSend, idReceive)
    }

    override fun resultLastMessage(message: String) {
        v.resultLastMessage(message)
    }
}