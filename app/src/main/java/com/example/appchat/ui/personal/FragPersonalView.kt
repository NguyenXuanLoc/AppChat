package com.example.appchat.ui.personal

import com.example.appchat.data.model.StatusModel

interface FragPersonalView {
    fun loadStatusSuccess(statusModel: StatusModel)
}