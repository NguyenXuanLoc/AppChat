package com.example.appchat.ui.me

import android.util.Log
import com.example.appchat.common.Constant
import com.example.appchat.common.Key
import com.example.appchat.data.model.StatusModel
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import timber.log.Timber

class MeFragmentModel(fragPersonalResponse: MeFragmentResponse) {
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
//                        v.loadStatusSuccess(results)
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
                        /*   if (results.size > 0) v.loadStatusSuccess(results)
                           else v.nullResult()*/
                    } else Log.e("TAG", "Not result+ lastNode: $lastNode")

                }
            })
    }

    fun loadMore(idUser: String, lastNode: String) {
        FirebaseDatabase.getInstance().getReference(Key.STATUS).child(idUser)
            .orderByKey()
            .limitToLast(Constant.PAGE_SIZE)
            .endAt(lastNode)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    var status = ArrayList<StatusModel>()
                    if (snapshot.hasChildren()) {
                        snapshot.children.forEach { it ->
                            var model = it.getValue<StatusModel>()
                            model?.let { it1 -> status.add(it1) }
                        }
                        v.loadMoreStatusSuccess(status)
                    } else v.nullResult()
                }
            })
    }

    fun getLastKey(idUser: String) {
        var lastKey: String? = null
        FirebaseDatabase.getInstance().getReference(Key.STATUS).child(idUser).orderByKey()
            .limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        snapshot.children.forEach { it ->
                            lastKey = it.key
                        }
                        lastKey?.let {
                            loadNewStatus(idUser, it)
                        }
                    } else {
                        v.nullResult()
                    }

                }
            })
    }

    fun loadNewStatus(idUser: String, lastNode: String) {
        FirebaseDatabase.getInstance().getReference(Key.STATUS).child(idUser)
            .orderByKey()
            .limitToLast(Constant.PAGE_SIZE)
            .endAt(lastNode)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.e("TAG", "ERROR: ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.e("TAG", "ERROR: ${snapshot.key}")
                    var status = ArrayList<StatusModel>()
                    if (snapshot.hasChildren()) {
                        snapshot.children.forEach { it ->
                            var model = it.getValue<StatusModel>()
                            model?.let { it1 -> status.add(it1) }
                        }
                        if (status.size > 0) {
                            v.loadNewStatusSuccess(status)
                        }
                    } else v.nullResult()
                }
            })
    }

    fun getFirstKey(idUser: String) {
        FirebaseDatabase.getInstance().getReference(Key.STATUS).child(idUser)
            .orderByKey().limitToFirst(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    var firstKey: String? = null
                    if (snapshot.hasChildren()) {
                        snapshot.children.forEach { it ->
                            firstKey = it.key
                        }
                    }
                    firstKey.run { v.loadFirstKeySuccess(this.toString()) }
                }
            })
    }
}