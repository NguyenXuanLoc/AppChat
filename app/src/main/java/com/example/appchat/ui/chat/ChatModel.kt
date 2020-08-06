package com.example.appchat.ui.chat

import com.example.appchat.common.Key
import com.example.appchat.data.model.MessageModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatModel(response: ChatResponse) {
    val v = response
    fun sendMessage(notChild: String, model: MessageModel) {
        var ref = FirebaseDatabase.getInstance().getReference(Key.CHATS).child(notChild)
        var key = ref.push().key
        ref.child(key.toString()).setValue(model)
    }

    fun checkNodeChild(idSend: String, idReceiver: String) {
        var node1 = idSend + idReceiver
        var node2 = idReceiver + idSend
        checkNode(node1.toString())
        checkNode(node2.toString())
    }

    // Check node
    private fun checkNode(node: String) {
        FirebaseDatabase.getInstance().reference.setValue(Key.CHATS)
        FirebaseDatabase.getInstance().getReference(Key.CHATS)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild(node)) {
                        v.loadNodeChildSuccess(node)
                    } else v.nullNodeChild()
                }
            })
    }
}