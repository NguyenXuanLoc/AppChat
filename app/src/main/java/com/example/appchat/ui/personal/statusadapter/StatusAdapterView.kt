package com.example.appchat.ui.personal.statusadapter

import com.example.appchat.data.model.ImageModel

interface StatusAdapterView {
    fun loadImageSuccess(results: ImageModel)
    fun test(images: ArrayList<ImageModel>)
}