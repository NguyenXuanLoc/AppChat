package com.example.appchat.ui.personal.statusadapter

import android.util.Log
import com.example.appchat.common.Key
import com.example.appchat.data.model.ImageModel
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import timber.log.Timber

class StatusAdapterModel(statusResponse: StatusAdapterResponse) {
    var v = statusResponse

    fun loadImages(idStatus: String) {
        FirebaseDatabase.getInstance().getReference(Key.IMAGE).child(idStatus)
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {
                        var model = snapshot.getValue<ImageModel>()
                        model?.let { v.loadImageSuccess(it) }
                    } else Log.e("TAG", "NULL")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                }
            })
    }
}