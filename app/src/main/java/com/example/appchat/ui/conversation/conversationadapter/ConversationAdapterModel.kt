package com.example.appchat.ui.conversation.conversationadapter

import android.util.Log
import com.example.appchat.common.Constant
import com.example.appchat.common.Key
import com.example.appchat.data.model.MessageModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class ConversationAdapterModel(response: ConversationAdapterResponse) {
    val v = response
    fun checkNodeChild(idSend: String, idReceiver: String) {
        var node1 = idSend + idReceiver
        var node2 = idReceiver + idSend
        checkNode(node1, idSend)
        checkNode(node2, idSend)
    }

    // Check node exist
    private fun checkNode(node: String, idUser: String) {
        FirebaseDatabase.getInstance().getReference(Key.CHATS).child(node)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        getLastMessage(node)
                        getCountMessageUnread(idUser, node)
                    }
                }
            })
    }

    // GetLastMessage
    private fun getLastMessage(node: String) {
        FirebaseDatabase.getInstance().getReference(Key.CHATS).child(node)
            .limitToLast(1)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        var model: MessageModel? = null
                        snapshot.children.forEach { it ->
                            model = it.getValue<MessageModel>()
                        }
                        model?.let { v.resultLastMessage(model!!) }
                    }
                }

            })
    }

    private fun getCountMessageUnread(idUser: String, node: String) {
        FirebaseDatabase.getInstance().getReference(Key.CHATS).child(node).orderByKey()
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        var count = 0
                        snapshot.children.forEach { it ->
                            var model = it.getValue<MessageModel>()
                            if (model != null) {
                                if (model.received == idUser && model.readMessage == Constant.UNREAD) {
                                    count++
                                }
                            }
                        }
                        v.resultCountUnread(count.toString())
                    } else {
                        v.resultCountUnread("0")
                    }
                }

            })
    }
}