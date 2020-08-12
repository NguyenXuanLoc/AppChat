package com.example.appchat.ui.conversation

import com.example.appchat.data.model.UserModel

interface ConversationFragmentView {
    fun loadFriendSuccess(list: ArrayList<UserModel>)
    fun nullResult()
}