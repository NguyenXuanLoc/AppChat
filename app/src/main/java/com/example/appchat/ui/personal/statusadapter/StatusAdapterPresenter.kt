package com.example.appchat.ui.personal.statusadapter

import com.example.appchat.data.model.ImageModel

class StatusAdapterPresenter(statusAdapterView: StatusAdapterView) : StatusAdapterResponse {
    private var v = statusAdapterView
    private var model = StatusAdapterModel(this)

    fun loadImage(idStatus: String) {
        model.loadImages(idStatus)
    }

    fun loadTest(idStatus: String) {
        model.test(idStatus)
    }

    override fun loadImageSuccess(results: ImageModel) {
        v.loadImageSuccess(results)
    }

    override fun test(images: ArrayList<ImageModel>) {
        v.test(images)
    }
}