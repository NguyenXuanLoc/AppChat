package com.example.appchat.ui.conversation.conversationadapter

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
        checkNode(node1)
        checkNode(node2)
    }

    // Check node exist
    private fun checkNode(node: String) {
        FirebaseDatabase.getInstance().getReference(Key.CHATS).child(node)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        getLastMessage(node)
                    }
                }
            })
    }

    private fun getLastMessage(node: String) {
        FirebaseDatabase.getInstance().getReference(Key.CHATS).child(node)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        var lastMessage: String? = null
                        snapshot.children.forEach { it ->
                            var model = it.getValue<MessageModel>()
                            lastMessage = model?.message
                        }
                        lastMessage?.let { v.resultLastMessage(lastMessage.toString()) }
                    }
                }

            })
    }
}