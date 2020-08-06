package com.example.appchat.ui.chat

import com.example.appchat.common.Constant
import com.example.appchat.common.Key
import com.example.appchat.data.model.MessageModel
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatModel(response: ChatResponse) {
    val v = response
    fun sendMessage(nodeChild: String, model: MessageModel) {
        var ref = FirebaseDatabase.getInstance().getReference(Key.CHATS).child(nodeChild)
        var key = ref.push().key
        val currentTime = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault())
        model.id = key
        model.time = currentTime.toString()
        ref.child(key.toString()).setValue(model)
    }

    fun checkNodeChild(idSend: String, idReceiver: String) {
        var node1 = idSend + idReceiver
        var node2 = idReceiver + idSend
        checkNode(node1)
        checkNode(node2)
    }

    fun loadMessage(node: String) {
        FirebaseDatabase.getInstance().getReference(Key.CHATS).child(node).orderByKey()
            .limitToFirst(Constant.PAGE_SIZE)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        var list = ArrayList<MessageModel>()
                        snapshot.children.forEach { it ->
                            var model = it.getValue<MessageModel>()
                            model?.let { it1 -> list.add(it1) }
                        }
                        v.loadMessageSuccess(list)
                    }
                }
            })
    }

    fun loadMoreMessage(node: String, lastNode: String) {
        FirebaseDatabase.getInstance().getReference(Key.CHATS).child(node).orderByKey()
            .startAt(lastNode)
            .limitToFirst(Constant.PAGE_SIZE)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        var list = ArrayList<MessageModel>()
                        snapshot.children.forEach { it ->
                            var model = it.getValue<MessageModel>()
                            model?.let { it1 -> list.add(it1) }
                        }
                        v.loadMoreSuccess(list)
                    }
                }
            })
    }

    fun test(nodeChild: String, lastNode: String) {
        FirebaseDatabase.getInstance().getReference(Key.CHATS).child(nodeChild).orderByKey()
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        var list = ArrayList<MessageModel>()
                        snapshot.children.forEach { it ->
                            var model = it.getValue<MessageModel>()
                            model?.let { it1 -> list.add(it1) }
                        }
                        v.loadMoreSuccess(list)
                    }
                }
            })
    }

    fun getNewMessage(node: String) {
        FirebaseDatabase.getInstance().getReference(Key.CHATS).child(node)
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {
                        var model = snapshot.getValue<MessageModel>()
                    }
                }

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                }
            })
    }

    // Check node exist
    private fun checkNode(node: String) {
        FirebaseDatabase.getInstance().getReference(Key.CHATS).child(node)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        v.loadNodeChildSuccess(node)
                        loadMessage(node)
                    } else v.nullNodeChild()
                }
            })
    }
}