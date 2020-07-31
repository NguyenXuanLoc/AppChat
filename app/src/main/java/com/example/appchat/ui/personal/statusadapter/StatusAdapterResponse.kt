package com.example.appchat.ui.personal.statusadapter

import android.media.Image
import com.example.appchat.data.model.ImageModel

interface StatusAdapterResponse {

    fun loadImageSuccess(results: ImageModel)
    fun test(images: ArrayList<ImageModel>)
}