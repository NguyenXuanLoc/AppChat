package com.example.appchat.ui.personal

import com.example.appchat.data.model.StatusModel

interface FragPersonalResponse {
    fun nullResult()
    fun resultLastKey(lastKey: String)
    fun loadStatusSuccess(results: ArrayList<StatusModel>)
    fun loadMoreStatusSuccess(results: ArrayList<StatusModel>)
}