package com.example.appchat.ui.personal

import com.example.appchat.data.model.StatusModel

class PersonalFragmentPresenter(fragPersonalView: PersonalFragmentView) : PersonalFragmentResponse {
    private val model = PersonalFragmentModel(this)
    private val v = fragPersonalView

    fun loadNewStatus(idUser: String) {
        model.getLastKey(idUser)
    }

    fun loadMore(idUser: String, lastNode: String) {
        model.loadMore(idUser, lastNode)
    }

    fun getFirstKey(idUser: String) {
        model.getFirstKey(idUser)
    }

    override fun loadMoreStatusSuccess(results: ArrayList<StatusModel>) {
        v.loadMoreSuccess(results)
    }

    override fun loadNewStatusSuccess(results: ArrayList<StatusModel>) {
        v.loadNewStatusSuccess(results)
    }

    override fun loadFirstKeySuccess(firstKey: String) {
        v.loadFirstKeySuccess(firstKey)
    }

    override fun nullResult() {
        v.nullResult()
    }


}