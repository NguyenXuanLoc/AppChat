package com.example.appchat.ui.listuser

import com.example.appchat.data.model.UserModel

class UserFragmentPresenter(view: UserFragmentView) : UserFragmentResponse {
    val v = view
    val model = UserFragmentModel(this)

    fun getFirstKey(node: String) {
        model.getFirstKey(node)
    }

    fun getLastKey(node: String) {
        model.getLastKey(node)
    }

    fun loadListUser(node: String, lastNode: String, isLoadMore: Boolean = false) {
        model.loadListUser(node, lastNode, isLoadMore)
    }

    override fun resultListUser(list: ArrayList<UserModel>) {
        v.resultListUser(list)
    }

    override fun resultLoadMoreListUser(list: ArrayList<UserModel>) {
        v.resultLoadMoreList(list)
    }

    override fun resultFirstKey(id: String) {
        v.resultFirstKey(id)
    }

    override fun resultNull() {
        v.resultNull()
    }

}