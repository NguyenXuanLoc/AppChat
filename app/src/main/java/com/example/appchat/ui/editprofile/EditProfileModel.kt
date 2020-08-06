package com.example.appchat.ui.editprofile

import com.example.appchat.common.Key
import com.example.appchat.data.model.UserModel
import com.google.firebase.database.FirebaseDatabase

class EditProfileModel(response: EditProfileResponse) {
    private val v = response
    public fun updateUser(model: UserModel) {
        FirebaseDatabase.getInstance().getReference(Key.USER).child(model.id.toString())
            .setValue(model).addOnSuccessListener {
                v.updateSuccess(model)
            }
    }
}