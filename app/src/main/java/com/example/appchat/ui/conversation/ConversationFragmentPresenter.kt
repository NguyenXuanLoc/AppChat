package com.example.appchat.ui.conversation

import com.example.appchat.data.model.UserModel

class ConversationFragmentPresenter(view: ConversationFragmentView) : ConversationFragmentResponse {
    val model = ConversationFragmentModel(this)
    val v = view
    fun getIdFriend(idUser: String) {
        model.getIdFriend(idUser)
    }

    override fun loadFriendSuccess(list: ArrayList<UserModel>) {
        v.loadFriendSuccess(list)
    }

    override fun nullResult() {
        v.nullResult()
    }
}