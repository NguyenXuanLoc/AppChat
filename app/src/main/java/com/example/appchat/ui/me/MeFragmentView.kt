package com.example.appchat.ui.me

import com.example.appchat.data.model.StatusModel

interface MeFragmentView {
    fun nullResult()
    fun loadMoreSuccess(results: ArrayList<StatusModel>)
    fun loadNewStatusSuccess(results: ArrayList<StatusModel>)
    fun loadFirstKeySuccess(firstKey: String)
}