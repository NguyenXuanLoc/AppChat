package com.example.appchat.ui.conversation.conversationadapter

import com.example.appchat.data.model.MessageModel

interface ConversationAdapterView {
    fun resultLastMessage(model: MessageModel)
    fun resultCountUnread(count: String)
}