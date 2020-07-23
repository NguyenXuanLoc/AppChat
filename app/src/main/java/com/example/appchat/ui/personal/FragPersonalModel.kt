package com.example.appchat.ui.personal

import android.util.Log
import com.example.appchat.common.Key
import com.example.appchat.data.model.StatusModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import timber.log.Timber

class FragPersonalModel(fragPersonalResponse: FragPersonalResponse) {
    var v = fragPersonalResponse

    fun getStatus(idUser: String) {
        FirebaseDatabase.getInstance().getReference(Key.STATUS).child(idUser)
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.e(error.message)
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

                }

                // Listener change of data
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    snapshot.getValue<StatusModel>()?.let { v.loadStatusSuccess(it) }

                }

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {
                        snapshot.getValue<StatusModel>()?.let { v.loadStatusSuccess(it) }
                        Log.e("TAG", snapshot.getValue<StatusModel>()?.status)
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {

                }
            })
    }
}