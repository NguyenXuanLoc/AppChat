package com.example.appchat.ui.chat.ChatAdapter

import android.util.Log
import com.example.appchat.common.Key
import com.example.appchat.data.model.ImageModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class ChatAdapterModel(response: ChatAdapterResponse) {
    private val v = response

    fun getPhoto(idMessage: String) {
        val list = ArrayList<ImageModel>()
        FirebaseDatabase.getInstance().getReference(Key.IMAGE).child(idMessage)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (list.size > 0) list.clear()
                    if (snapshot.hasChildren()) {
                        snapshot.children.forEach { it ->
                            var model = it.getValue<ImageModel>()
                            model?.let { it1 -> list.add(it1) }
                        }
                        v.loadPhotosSuccess(list)
                    } else {
                        v.loadPhotosSuccessNull()
                    }

                }
            })
    }
}