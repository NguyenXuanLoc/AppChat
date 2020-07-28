package com.example.appchat.ui.personal.statusadapter

import com.example.appchat.data.model.ImageModel

interface StatusAdapterResponse {

    fun loadImageSuccess(results: ImageModel)
}