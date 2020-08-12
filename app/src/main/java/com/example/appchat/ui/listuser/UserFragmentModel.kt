package com.example.appchat.ui.listuser

import com.example.appchat.common.Constant
import com.example.appchat.common.Key
import com.example.appchat.data.model.OptionModel
import com.example.appchat.data.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class UserFragmentModel(response: UserFragmentResponse) {
    val v = response
    fun getLastKey(node: String) {
        FirebaseDatabase.getInstance().getReference(node).orderByKey().limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    var lastKey = ""
                    if (snapshot.hasChildren()) {
                        snapshot.children.forEach { it ->
                            lastKey = it.key.toString()
                        }
                        loadListUser(node, lastKey)
                    }
                }
            })
    }

    fun getFirstKey(node: String) {
        FirebaseDatabase.getInstance().getReference(node).orderByKey().limitToFirst(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    var key: String? = null
                    if (snapshot.hasChildren()) {
                        snapshot.children.forEach {
                            key = it.key
                        }
                        key.run { v.resultFirstKey(this.toString()) }
                    }
                }
            })
    }

    fun loadListUser(node: String, lastNode: String, isLoadMore: Boolean = false) {
        FirebaseDatabase.getInstance().getReference(node).orderByKey()
            .limitToLast(Constant.PAGE_SIZE)
            .endAt(lastNode)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        var list = ArrayList<UserModel>()
                        snapshot.children.forEach { it ->
                            var model = it.getValue<UserModel>()
                            model?.let { it1 -> list.add(it1) }
                        }
                        if (list.size > 0) {
                            if (isLoadMore) {
                                v.resultLoadMoreListUser(list)
                            } else {
                                v.resultListUser(list)
                            }
                        }
                    } else v.resultNull()
                }
            })
    }

    fun loadOptions() {
        FirebaseDatabase.getInstance().getReference(Key.OPTIONS).orderByKey()
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        var list = ArrayList<OptionModel>()
                        snapshot.children.forEach { it ->
                            var model = it.getValue<OptionModel>()
                            model?.let { it1 -> list.add(it1) }
                        }
                        v.resultOption(list)
                    }
                }
            })
    }
}