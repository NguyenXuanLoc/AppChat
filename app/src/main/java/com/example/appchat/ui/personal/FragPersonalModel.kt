package com.example.appchat.ui.personal

import android.util.Log
import com.example.appchat.common.Constant
import com.example.appchat.common.Key
import com.example.appchat.data.model.StatusModel
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import timber.log.Timber

class FragPersonalModel(fragPersonalResponse: FragPersonalResponse) {
    var v = fragPersonalResponse

    fun getStatus(idUser: String) {
        FirebaseDatabase.getInstance().getReference(Key.STATUS).child(idUser)
            .limitToFirst(Constant.PAGE_SIZE)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.e(error.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    var results = ArrayList<StatusModel>()
                    results.clear()
                    if (snapshot.hasChildren()) {
                        snapshot.children.forEach { it ->
                            var statusModel = it.getValue<StatusModel>()
                            statusModel?.let { results.add(it) }
                        }
                        v.loadStatusSuccess(results)
                    }

                }
            })
    }

    fun test(idUser: String, lastNode: String) {
        FirebaseDatabase.getInstance().getReference(Key.STATUS).child(idUser)
            .startAt(lastNode)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.e(error.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        var results = ArrayList<StatusModel>()
                        results.clear()
                        snapshot.children.forEach { it ->
                            var statusModel = it.getValue<StatusModel>()
                            statusModel?.let { results.add(it) }
                        }
                        if (results.size > 0) v.loadStatusSuccess(results)
                        else v.nullResult()
                    } else Log.e("TAG", "Not result+ lastNode: $lastNode")

                }
            })
    }

    fun loadMoreStatus(idUser: String, lastNode: String) {
        FirebaseDatabase.getInstance().getReference(Key.STATUS).child(idUser)
            .orderByKey().startAt(lastNode)
            .limitToFirst(Constant.PAGE_SIZE)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.e("TAG", error.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        var status = ArrayList<StatusModel>()
                        if (status.size > 0) status.clear()
                        snapshot.children.forEach { it ->
                            var model = it.getValue<StatusModel>()
                            model?.let { it1 -> status.add(it1) }
                        }
                        if (status.size <= 1) v.nullResult()
                        else v.loadMoreStatusSuccess(status)
                    } else v.nullResult()
                }
            })
    }

    fun getLastKey(idUser: String) {
        var lastKey: String? = ""
        FirebaseDatabase.getInstance().getReference(Key.STATUS).child(idUser).orderByKey()
            .limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.e("TAG", "CAN NOT GET LAST KEY: ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        snapshot.children.forEach { it ->
                            lastKey = it.key
                        }
                        if (lastKey?.isNotEmpty()!!)
                            v.resultLastKey(lastKey.toString())
                        else {
                        }
                    }

                }
            })
    }

}