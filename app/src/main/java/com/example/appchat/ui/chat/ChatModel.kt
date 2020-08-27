package com.example.appchat.ui.chat

import android.annotation.SuppressLint
import android.net.Uri
import com.example.appchat.common.Constant
import com.example.appchat.common.Key
import com.example.appchat.data.model.*
import com.example.appchat.ui.fcm.APIService
import com.example.appchat.ui.fcm.Client
import com.example.appchat.ui.fcm.Data
import com.example.appchat.ui.fcm.NotificationSender
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatModel(response: ChatResponse) {
    var status: String? = null
    val v = response
    var token: String? = null

    //if Revieve offline, send and push notification
    fun sendMessage(
        nodeChild: String,
        model: MessageModel,
        useSend: UserModel,
        urlsPhoto: ArrayList<String>? = null,
        uriAudio: Uri? = null
    ) {
        var ref = FirebaseDatabase.getInstance().getReference(Key.CHATS).child(nodeChild)
        var key = ref.push().key
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("hh:mm", Locale.getDefault()).format(Date())
        model.id = key
        model.time = currentTime.toString()
        model.date = currentDate.toString()
        ref.child(key.toString()).setValue(model).addOnSuccessListener {
            urlsPhoto?.let {
                uploadPhoto(it, model.id.toString())
            }
            uriAudio?.let {
                uploadAudio(it, model.id.toString(), nodeChild)
            }
            if (status == Constant.OFFLINE) {
                token?.let { it1 ->
                    sendNotification(
                        it1, useSend.userName.toString(),
                        model.message.toString(), model.sender.toString()
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

    @SuppressLint("CheckResult")
    private fun sendNotification(
        userToken: String, title: String, message: String, idSender: String
    ) {
        val data = Data(title, message, idSender)
        val sender = NotificationSender(data, userToken)
        var client = Client()
        var apiService = client.getClient(Constant.URL_FCM)!!.create(APIService::class.java)
        apiService?.sendNotification(sender)?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                if (it.success != 1) {
                    Timber.e("False push notification")
                }
            }, {
                Timber.e(it.message)
            })
    }

    fun checkNodeChild(idUser: String, idReceiver: String) {
        var node1 = idUser + idReceiver
        var node2 = idReceiver + idUser
        checkNode(node1, idUser)
        checkNode(node2, idUser)
    }

    fun readMessage(idMessage: String, node: String) {
        FirebaseDatabase.getInstance().getReference(Key.CHATS)
            .child(node).child(idMessage).child(Key.READ_MESSAGE)
            .setValue(Key.READ)
    }

    fun loadOldMessage(node: String, topNode: String) {
        var list = ArrayList<MessageModel>()
        FirebaseDatabase.getInstance().getReference(Key.CHATS).child(node)
            .orderByKey()
            .endAt(topNode)
            .limitToLast(Constant.PAGE_SIZE)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        list.clear()
                        snapshot.children.forEach { it ->
                            var model = it.getValue<MessageModel>()
                            if (model?.id != topNode && model != null) {
//                                Log.e("TAG", "OLD MESSAGE: ${model.id.toString()}")
                                list.add(model)
                            }
                        }
                        if (list.size > 0) {
                            v.loadOldMessageSuccess(list)
                        } else {
                            v.nullOldMessage()
                        }
                    }
                }
            })
    }

    // Check node exist and get last message
    private fun checkNode(node: String, idUser: String) {
        //count check if count = 2 -> nullNodeChild
        FirebaseDatabase.getInstance().getReference(Key.CHATS).child(node)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        v.loadNodeChildSuccess(node)
                        loadMessage(node)
                    } else {
                        v.nullNodeChild()
                    }
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

    //getNew Message
    fun loadNewMessage(node: String) {
        FirebaseDatabase.getInstance().getReference(Key.CHATS).child(node).orderByKey()
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
                        model?.let { v.loadNewMessageSuccess(it) }
                    }
                }
            })
    }

    // get first message
    fun loadMessage(node: String) {
        FirebaseDatabase.getInstance().getReference(Key.CHATS).child(node).orderByKey()
            .limitToLast(Constant.PAGE_SIZE)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    var list = ArrayList<MessageModel>()
                    if (snapshot.hasChildren()) {
                        snapshot.children.forEach { it ->
                            var model = it.getValue<MessageModel>()
                            if (model?.readMessage == Constant.UNREAD) {
                                readMessage(model.id.toString(), node)
                            }
                            model?.let { it1 -> list.add(it1) }
                        }
                        v.loadFirstMessageSuccess(list)
                    } else v.nullNodeChild()
                }

            })
    }

    fun loadNewGift() {
        var list = ArrayList<GifModel>()
        FirebaseDatabase.getInstance().getReference(Key.GIF).limitToFirst(Constant.PAGE_SIZE)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (list.size > 0) list.clear()
                    if (snapshot.hasChildren()) {
                        snapshot.children.forEach { it ->
                            var model = it.getValue<GifModel>()
                            model?.let { list.add(model) }
                        }
                        v.loadGifSuccess(list)
                    }
                }

            })
    }

    fun loadMoreGift(lastNode: String) {
        val list = ArrayList<GifModel>()
        FirebaseDatabase.getInstance().getReference(Key.GIF)
            .startAt(lastNode).orderByKey().limitToFirst(Constant.PAGE_SIZE)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        snapshot.children.forEach { it ->
                            var model = it.getValue<GifModel>()
                            if (model != null && model.id != lastNode) {
                                list.add(model)
                            }
                        }
                        v.loadMoreGifSuccess(list)
                    }
                }

            })
    }

    private fun uploadPhoto(urlPicks: ArrayList<String>, idMessage: String) {
        for (i in 0 until urlPicks.size) {
            var uri = Uri.fromFile(File(urlPicks[i]))
            var mStorageRef = FirebaseStorage.getInstance().reference
            var nameFile = System.currentTimeMillis().toString()
            mStorageRef.child(Key.IMAGE).child(nameFile).putFile(uri).addOnSuccessListener { it ->
                var result = it.metadata?.reference?.downloadUrl
                result?.addOnSuccessListener {
                    var url = it.toString()
                    var imageModel = ImageModel(nameFile, idMessage, url)
                    FirebaseDatabase.getInstance().getReference(Constant.IMAGE).child(idMessage)
                        .push()
                        .setValue(imageModel)
                }
            }
        }
    }

    private fun uploadAudio(uriAudio: Uri, idMessage: String, nodeChild: String) {
        var ref = FirebaseStorage.getInstance().getReference(Key.AUDIO)
        var nameFile = System.currentTimeMillis().toString()
        ref.child(nameFile).putFile(uriAudio).addOnSuccessListener {
            it.metadata?.reference?.downloadUrl?.addOnSuccessListener { it ->
                var url = it.toString()
                Timber.e(it.toString())
                FirebaseDatabase.getInstance().getReference(Key.CHATS).child(nodeChild)
                    .child(idMessage).child(Key.URL_AUDIO).setValue(url)
            }
        }
    }

    @SuppressLint("CheckResult")
    fun pushNotifyCallVideo(
        userToken: String, nameSender: String, message: String, idSender: String
    ) {
        val data = Data(nameSender, message, idSender, Key.VOICE_CALL)
        val sender = NotificationSender(data, userToken)
        var client = Client()
        var apiService = client.getClient(Constant.URL_FCM)!!.create(APIService::class.java)
        apiService?.sendNotification(sender)?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                if (it.success != 1) {
                    Timber.e("False push notification")
                }
            }, {
                Timber.e(it.message)
            })
    }
}