package com.example.appchat.ui.editprofile

import com.example.appchat.data.model.UserModel

interface EditProfileResponse {
    fun updateSuccess(model: UserModel)
}