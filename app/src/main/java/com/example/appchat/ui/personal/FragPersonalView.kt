package com.example.appchat.ui.personal

import com.example.appchat.data.model.StatusModel

interface FragPersonalView {
    fun nullResult()
    fun resultLastKey(lastKey: String)
    fun loadStatusSuccess(results: ArrayList<StatusModel>)
    fun loadMoreSuccess(results: ArrayList<StatusModel>)
}