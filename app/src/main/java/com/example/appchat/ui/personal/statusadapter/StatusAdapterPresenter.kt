package com.example.appchat.ui.personal.statusadapter

import com.example.appchat.data.model.VideoModel

class StatusAdapterPresenter(statusAdapterView: StatusAdapterView) : StatusAdapterResponse {
    private var v = statusAdapterView
    private var model = StatusAdapterModel(this)

    fun loadVideo(idStatus: String, itemHolder: StatusAdapter.ItemHolder) {
        model.loadVideo(idStatus, itemHolder)
    }


    override fun loadVideoSuccess(videoModel: VideoModel, itemHolder: StatusAdapter.ItemHolder) {
        v.loadVideoSuccess(videoModel, itemHolder)
    }
}