package com.example.appchat.ui.editprofile

import com.example.appchat.data.model.UserModel

class EditProfilePresenter(view: EditProfileView) : EditProfileResponse {
    val model = EditProfileModel(this)
    val v = view
    fun updateUser(model: UserModel) {
        this.model.updateUser(model)
    }

    override fun updateSuccess(model: UserModel) {
        v.updateSuccess(model)
    }
}