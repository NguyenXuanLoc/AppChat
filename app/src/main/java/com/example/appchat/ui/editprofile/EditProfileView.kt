package com.example.appchat.ui.editprofile

import com.example.appchat.data.model.UserModel

interface EditProfileView {
    fun updateSuccess(model: UserModel)
}