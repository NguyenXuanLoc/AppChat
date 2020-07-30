package com.example.appchat.ui.personal

import com.example.appchat.data.model.StatusModel

class FragPersonalPresenter(fragPersonalView: FragPersonalView) : FragPersonalResponse {
    private val model = FragPersonalModel(this)
    private val v = fragPersonalView

    fun getStatus(idUser: String) {
        model.getStatus(idUser)
    }

    fun getLastKey(idUser: String) {
        model.getLastKey(idUser)
    }

    fun loadMore(idUser: String, lastNode: String) {
        model.loadMoreStatus(idUser, lastNode)
    }

    fun loadNewStatus(idUser: String) {
    model.loadNewStatus(idUser)
    }

    override fun loadStatusSuccess(results: ArrayList<StatusModel>) {
        v.loadStatusSuccess(results)
    }

    override fun loadMoreStatusSuccess(results: ArrayList<StatusModel>) {
        v.loadMoreSuccess(results)
    }

    override fun loadNewStatusSuccess(results: ArrayList<StatusModel>) {
        v.loadNewStatusSuccess(results)
    }

    override fun nullResult() {
        v.nullResult()
    }

    override fun resultLastKey(lastKey: String) {
        v.resultLastKey(lastKey)
    }
}