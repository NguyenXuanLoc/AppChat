package com.example.appchat.ui.fcm

import com.example.appchat.common.Key
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseIdService : FirebaseMessagingService() {
    override fun onNewToken(p0: String) {
        super.onNewToken(p0!!)
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val refreshToken = FirebaseInstanceId.getInstance().token
        if (firebaseUser != null) {
            refreshToken?.let { updateToken(it) }
        }
    }

    private fun updateToken(refreshToken: String) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val token1 =
            Token(refreshToken)
        FirebaseDatabase.getInstance().getReference(Key.TOKENS).child(firebaseUser!!.uid)
            .setValue(token1)
    }
}