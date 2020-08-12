package com.example.appchat.ui.me.statusadapter

import com.example.appchat.data.model.ImageModel

interface StatusAdapterView {
    fun loadImageSuccess(results: ImageModel)
    fun test(images: ArrayList<ImageModel>)
}