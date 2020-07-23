package com.example.appchat.ui.personal

import com.example.appchat.data.model.StatusModel

class FragPersonalPresenter(fragPersonalView: FragPersonalView) : FragPersonalResponse {
    private val model = FragPersonalModel(this)
    private val v = fragPersonalView
    fun getStatus(idUser: String) {
        model.getStatus(idUser)
    }

    override fun loadStatusSuccess(statusModel: StatusModel) {
        v.loadStatusSuccess(statusModel)
    }
}