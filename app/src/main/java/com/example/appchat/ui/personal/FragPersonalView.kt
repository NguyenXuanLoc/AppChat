package com.example.appchat.ui.personal

import com.example.appchat.data.model.StatusModel

interface FragPersonalView {
    fun nullResult()
    fun loadMoreSuccess(results: ArrayList<StatusModel>)
    fun loadNewStatusSuccess(results: ArrayList<StatusModel>)
    fun loadFirstKeySuccess(firstKey: String)
}