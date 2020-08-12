package com.example.appchat.ui.me.statusadapter

import android.util.Log
import com.example.appchat.common.Key
import com.example.appchat.data.model.ImageModel
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class StatusAdapterModel(statusResponse: StatusAdapterResponse) {
    var v = statusResponse
    fun test(idStatus: String) {
        FirebaseDatabase.getInstance().getReference(Key.IMAGE).child(idStatus)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    var images = ArrayList<ImageModel>()
                    if (snapshot.hasChildren()) {
                        snapshot.children.forEach { it ->
                            var model = it.getValue<ImageModel>()
                            model?.let { it1 -> images.add(it1) }
                            Log.e("TAG: OK", model?.url.toString())
                        }
                        if (images.size > 0) {
                            v.test(images)
                        }
                    }
                }

            })
    }

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