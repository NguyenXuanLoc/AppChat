package com.example.appchat.ui.chat

import android.util.Log
import com.example.appchat.common.Constant
import com.example.appchat.common.Key
import com.example.appchat.data.model.MessageModel
import com.example.appchat.data.model.UserModel
import com.example.appchat.ui.fcm.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatModel(response: ChatResponse) {
    var status: String? = null
    val v = response
    var token: String? = null

    fun sendMessage(nodeChild: String, model: MessageModel, userReceive: UserModel) {
        var ref = FirebaseDatabase.getInstance().getReference(Key.CHATS).child(nodeChild)
        var key = ref.push().key
        val currentTime = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault())
        model.id = key
        model.time = currentTime.toString()
        ref.child(key.toString()).setValue(model).addOnSuccessListener {
            if (status == Constant.OFFLINE) {
                token?.let { it1 ->
                    sendNotification(
                        it1, userReceive.userName.toString(),
                        model.message.toString(), userReceive.id.toString()
                    )
                }
            }
        }
    }

    // if return true: Online , return false: Offline and push notification
     fun checkStatus(user: UserModel) {
        FirebaseDatabase.getInstance().getReference(Key.USER).child(user.id.toString())
            .child(Key.STATUS).addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    status = snapshot.getValue<String>().toString()
                }
            })
    }

    private fun sendNotification(
        userToken: String,
        title: String,
        message: String, idReceive: String
    ) {
        val data = Data(title, message, idReceive)
        val sender = NotificationSender(data, userToken)
        var client = Client()
        var apiService =
            client.getClient(Constant.URL_FCM)?.create(APIService::class.java)
        apiService?.sendNotification(sender)?.enqueue(object : Callback<MyResponse> {
            override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                Log.e("TAG", t.message)
            }

            override fun onResponse(call: Call<MyResponse>, response: Response<MyResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.success != 1) {
                        Timber.e("Error Push")
                    }
                }
            }
        })
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

    fun loadMoreMessage(node: String, lastNode: String, isNew: Boolean = true) {
        var list = ArrayList<MessageModel>()
        FirebaseDatabase.getInstance().getReference(Key.CHATS).child(node)
            .orderByKey()
            .startAt(lastNode)
            .limitToFirst(Constant.PAGE_SIZE)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        list.clear()
                        snapshot.children.forEach { it ->
                            var model = it.getValue<MessageModel>()
                            if (model?.id != lastNode) {
                                model?.let { it1 -> list.add(it1) }
                            }
                        }
                        if (isNew) {
                            v.loadMoreSuccess(list)
                        } else {
                            v.loadNewMessageSuccess(list)
                        }
                    }
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

    // Get Token Receive
    fun getToken(userModel: UserModel) {
        FirebaseDatabase.getInstance().getReference(Key.TOKENS).child(userModel.id.toString())
            .child(Key.ID_TOKEN).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    var tokenReceive = snapshot.getValue<String>()
                    token = tokenReceive
                    tokenReceive?.let { v.loadTokenSuccess(it) }
                }

            })
    }
}