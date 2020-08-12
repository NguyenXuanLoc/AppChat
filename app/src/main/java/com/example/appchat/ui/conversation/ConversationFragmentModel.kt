package com.example.appchat.ui.conversation

import com.example.appchat.common.Key
import com.example.appchat.data.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class ConversationFragmentModel(response: ConversationFragmentResponse) {
    val v = response
    fun getIdFriend(idUser: String) {
        var list = ArrayList<String>()
        FirebaseDatabase.getInstance().getReference(Key.CHATS)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        list.clear()
                        snapshot.children.forEach { it ->
                            var idFriend = it.key?.replace(idUser, "")
                            list.add(idFriend.toString())
                        }
                        if (list.size > 0) {
                            getProfileFriend(list)
                        }
                    } else {
                        v.nullResult()
                    }
                }
            })
    }

    fun getProfileFriend(list: ArrayList<String>) {
        var users = ArrayList<UserModel>()
        for (i in 0 until list.size) {
            FirebaseDatabase.getInstance().getReference(Key.USER).child(list[i])
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            var model = snapshot.getValue<UserModel>()
                            model?.let {
                                users.add(it)
                            }
                        }
                        if (i == (list.size - 1)) {
                            v.loadFriendSuccess(users)
                        }
                    }

                })
        }

    }

}