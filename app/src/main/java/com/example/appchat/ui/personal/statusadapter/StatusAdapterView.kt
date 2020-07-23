package com.example.appchat.ui.personal.statusadapter

import com.example.appchat.data.model.VideoModel

interface StatusAdapterView {
    fun loadVideoSuccess(videoModel: VideoModel,itemHolder: StatusAdapter.ItemHolder)
}