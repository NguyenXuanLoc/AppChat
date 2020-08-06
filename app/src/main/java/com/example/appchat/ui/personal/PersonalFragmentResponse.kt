package com.example.appchat.ui.personal

import com.example.appchat.data.model.StatusModel

interface PersonalFragmentResponse {
    fun nullResult()
    fun loadMoreStatusSuccess(results: ArrayList<StatusModel>)
    fun loadNewStatusSuccess(results: ArrayList<StatusModel>)
    fun loadFirstKeySuccess(firstKey: String)
}