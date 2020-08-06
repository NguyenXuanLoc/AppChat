package com.example.appchat.ui.home

import com.example.appchat.common.Key
import com.google.firebase.database.FirebaseDatabase

class HomeActivityModel(response: HomeActivityResponse) {
    private val v = response

    // UpLoadStatus Online or Offline
    fun upLoadStatus(idUser: String, status: String) {
        FirebaseDatabase.getInstance().getReference(Key.USER).child(idUser).child(Key.STATUS)
            .setValue(status )
    }
}