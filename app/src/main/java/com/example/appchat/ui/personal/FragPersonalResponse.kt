package com.example.appchat.ui.personal

import com.example.appchat.data.model.StatusModel

interface FragPersonalResponse {
    fun loadStatusSuccess(statusModel: StatusModel)
}