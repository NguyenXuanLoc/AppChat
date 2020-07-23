package com.example.appchat.ui.personal.statusadapter

import com.example.appchat.common.Key
import com.example.appchat.data.model.StatusModel
import com.example.appchat.data.model.VideoModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import timber.log.Timber

class StatusAdapterModel(statusResponse: StatusAdapterResponse) {
    var v = statusResponse

    fun loadVideo(idStatus: String, itemHolder: StatusAdapter.ItemHolder) {
        FirebaseDatabase.getInstance().getReference(Key.VIDEO).child(idStatus)
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.e(error.message)
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

                }

                // Listener change of data
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    snapshot.getValue<StatusModel>()?.let { }
                }

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {
                        snapshot.getValue<VideoModel>()?.let {
                            v.loadVideoSuccess(it, itemHolder)
                        }
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {

                }
            })
    }
}