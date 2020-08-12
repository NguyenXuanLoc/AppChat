package com.example.appchat.ui.me

import com.example.appchat.data.model.StatusModel

interface MeFragmentResponse {
    fun nullResult()
    fun loadMoreStatusSuccess(results: ArrayList<StatusModel>)
    fun loadNewStatusSuccess(results: ArrayList<StatusModel>)
    fun loadFirstKeySuccess(firstKey: String)
}